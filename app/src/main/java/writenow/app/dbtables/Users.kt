package writenow.app.dbtables

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

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
        private const val URL = "http://write-now.lesspopmorefizz.com/api/user"
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

        private fun post(section: String, json: JSONObject, callback: (Boolean) -> Unit) {
            val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
            val req = Request.Builder().url("$URL/$section").post(body).build()

            client.newCall(req).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("API Error", e.toString())

                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    Log.d("Response message", response.message)
                    Log.d("Response code", response.code.toString())

                    if (response.isSuccessful) {
                        Log.d("API Success", "Posted successfully")

                        callback(true)
                    } else {
                        Log.e("API Error", "Unexpected response code: ${response.code}")
                        Log.e("API Error", responseBody ?: "No response body")

                        callback(false)
                    }

                    response.close().apply { Log.d("API Success", "Response closed") }
                }
            })
        }

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

        suspend fun getEmails(): List<String> {
            return getAll().map { it.email }
        }

        fun register(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            post("register", jsonObject, callback)
        }

        fun login(username: String, password: String, callback: (Boolean) -> Unit) {
            val json = JSONObject().apply {
                put("username", username)
                put("password", password)
            }

            post("login", json, callback)
        }
    }
}
