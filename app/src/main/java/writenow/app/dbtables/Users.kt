package writenow.app.dbtables

import android.util.Log
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

class Users private constructor() : DBUtils("user") {
    companion object {
        private val utils = DBUtils("user")

        suspend fun getAll(): List<User> {
            return utils.getAll {
                User(
                    it.getInt("uuid"),
                    it.getString("username"),
                    it.getString("firstName"),
                    it.getString("lastName"),
                    it.getString("email"),
                    it.getString("passwordHash"),
                )
            }
        }

        suspend inline fun <reified T> get(prop: String): List<T> {
            return getAll().map { it[prop] }.filterIsInstance<T>()
        }

        suspend fun getEmails(): List<String> {
            return getAll().map { it.email }
        }

        fun register(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("register", jsonObject, callback)
        }

        suspend fun login(username: String, password: String, callback: (Boolean, User?) -> Unit) {
            val json = JSONObject().apply {
                put("username", username)
                put("password", password)
            }
            val user = getAll().find { it.username.equals(username, true) }

            utils.post("login", json) {
                Log.d("login2", "it: $it, user: $user")
                if (it && user != null) callback(true, user)
                else callback(false, null)
            }
        }
    }
}
