package com.example.myapplication.dbtables

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
) {
    operator fun get(s: String): Any? {
        return when (s) {
            "id" -> id
            "username" -> username
            "firstName" -> firstName
            "lastName" -> lastName
            "email" -> email
            "passwordHash" -> passwordHash
            else -> null
        }
    }
}

class Users private constructor() {
    companion object {
        private const val URL = "http://www.write-now.lesspopmorefizz.com"
        private val client = OkHttpClient()

        private suspend fun getJson(): String? = CoroutineScope(Dispatchers.IO).async {
            val request = Request.Builder().url(URL).build()

            return@async try {
                val response = client.newCall(request).execute()
                val json = response.body?.string()

                response.close()
                json
            } catch (e: Exception) {
                Log.e("API Error", e.toString())
                null
            }
        }.await()

        suspend fun getAll(): List<User> {
            val json = getJson()

            return json?.let {
                val jsonArray = JSONArray(it)

                (0 until jsonArray.length()).map { i ->
                    val userJson = jsonArray.getJSONObject(i)
                    User(
                        userJson.getInt("uuid"),
                        userJson.getString("username"),
                        userJson.getString("firstName"),
                        userJson.getString("lastName"),
                        userJson.getString("email"),
                        userJson.getString("passwordHash"),
                    )
                }
            } ?: emptyList()
        }

        suspend inline fun <reified T> get(prop: String): List<T> {
            return getAll().map { it[prop] }.filterIsInstance<T>()
        }

        suspend fun getById(id: Int): User? {
            return getAll().firstOrNull { it.id == id }
        }

        suspend fun getEmails(): List<String> {
            return getAll().map { it.email }
        }

        suspend fun getUsernames(): List<String> {
            return getAll().map { it.username }
        }

        suspend fun getPasswords(): List<String> {
            return getAll().map { it.passwordHash }
        }

        fun insert(json: JSONObject): Boolean {
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val req = Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build()
            val buffer = Buffer()

            body.writeTo(buffer)
            Log.d("Body", buffer.readUtf8())

            try {
                val response = client.newCall(req).execute()
                val responseBody = response.body?.string()

                Log.d("Response message", response.message)
                Log.d("Response code", response.code.toString())

                return if (response.isSuccessful) {
                    true
                } else {
                    Log.e("API Error", "Unexpected response code: ${response.code}")
                    Log.e("API Error", responseBody ?: "No response body")
                    false
                }
            } catch (e: Exception) {
                Log.e("API Error", e.toString())

                return false
            }
        }

        fun insert2(json: JSONObject) {
            val url = URL(this.URL)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000

            // set request headers
            connection.setRequestProperty("Content-Type", "application/json")

            // send post data
            connection.doOutput = true
            val outputStream = DataOutputStream(connection.outputStream)
            outputStream.writeBytes(json.toString())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            println("POST Response Code :: $responseCode")

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuffer()

                var inputLine: String?
                while (inputStream.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                inputStream.close()

                // print result
                println(response.toString())
            } else {
                println("POST request failed")
            }
        }

        fun insert3(json: JSONObject) {
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val req = Request.Builder().url(URL).post(body).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("API Error", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    Log.d("Response message", response.message)
                    Log.d("Response code", response.code.toString())

                    if (response.isSuccessful) {
                        Log.d("API Success", "User inserted")
                    } else {
                        Log.e("API Error", "Unexpected response code: ${response.code}")
                        Log.e("API Error", responseBody ?: "No response body")
                    }

                    response.close().apply { Log.d("API Success", "Response closed") }
                }
            })
        }
    }
}
