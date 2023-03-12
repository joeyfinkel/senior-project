package writenow.app.dbtables

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
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

        private suspend fun post(section: String, json: JSONObject): Boolean =
            CoroutineScope(Dispatchers.IO).async {
                val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val req = Request.Builder().url("$URL/$section").post(body).build()

                client.newCall(req).execute()

                return@async try {
                    val response = client.newCall(req).execute()
                    val isSuccess = response.isSuccessful

                    Log.d("Res", "Posted data")
                    response.close()
                    isSuccess
                } catch (e: Exception) {
                    Log.e("API Error", e.toString())
                    false
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

        suspend fun getEmails(): List<String> {
            return getAll().map { it.email }
        }

        fun register(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            runBlocking {
                val res = withContext(Dispatchers.Default) {
                    post("register", jsonObject)
                }

                callback(res)
            }
        }

        suspend fun login(username: String, password: String): Boolean {
            val json = JSONObject().apply {
                put("username", username)
                put("password", password)
            }

            return post("login", json)
        }
    }
}
