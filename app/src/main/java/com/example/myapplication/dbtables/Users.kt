package com.example.myapplication.dbtables

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject

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
        private val client = OkHttpClient.Builder().build()

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
    }
}
