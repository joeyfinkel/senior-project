package writenow.app.dbtables

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.Builder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import writenow.app.state.UserState
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator

data class Relationship(val sourceFriend: Int, val targetFriend: Int)
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
        private val utils = DBUtils("user")
        private const val SHARED_PREFS_FILENAME = "user_login_info"
        private const val KEY_ALIAS = "user_login_info_key"

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
                if (it && user != null) callback(true, user)
                else callback(false, null)
            }
        }

        private fun getSharedPreferences(context: Context): SharedPreferences {
            val masterKey = Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

            return EncryptedSharedPreferences.create(
                context,
                SHARED_PREFS_FILENAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        suspend fun saveLoginInfo(context: Context, user: UserState) {
            withContext(Dispatchers.IO) {
                try {
                    val sharedPreferences = getSharedPreferences(context)
                    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
                    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                        KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    ).apply {
                        setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        setUserAuthenticationRequired(false)
                        setKeySize(256)
                    }.build()

                    keyGenerator.init(keyGenParameterSpec)
                    keyGenerator.generateKey()

                    // Encrypt the user's login information using the AES key, and save it to the
                    // encrypted shared preferences file.
                    sharedPreferences.edit().apply {
                        putString("username", user.username)
                        putString("password", user.password)
                        putString("id", user.id.toString())
                        apply()
                    }
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: InvalidAlgorithmParameterException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }

        suspend fun getUserLoginInfo(context: Context) {
            withContext(Dispatchers.IO) {
                try {
                    val sharedPreferences = getSharedPreferences(context)
                    val username = sharedPreferences.getString("username", "")
                    val password = sharedPreferences.getString("password", "")
                    val id = sharedPreferences.getString("id", "")

                    if (username != null && password != null) {
                        UserState.username = "$username"
                        UserState.password = "$password"
                        UserState.isLoggedIn = true

                        if (id != null) {
                            UserState.id = id.toInt()
                        }

                        Log.d("User is logged in", "true")
                        UserState.getHasPosted()
                    }

                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: InvalidAlgorithmParameterException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }

        suspend fun clearLoginInfo(context: Context) {
            withContext(Dispatchers.IO) {
                try {
                    val sharedPreferences = getSharedPreferences(context)

                    sharedPreferences.edit().clear().apply()
                    Log.d("Cleared login info", "true")
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: InvalidAlgorithmParameterException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }

        /**
         * Checks if the current user is following the selected user.
         * The callback returns true if the current user is following the selected user.
         *
         * @param callback The callback function to be called after the database query is complete.
         * @return Boolean The boolean value of whether the current user is following the selected user.
         */
        suspend fun isFollowing(sourceFriend: Int, targetFriend: Int): Boolean {
            val relationship = utils.getAll("followers") {
                Relationship(it.getInt("sourceFriend"), it.getInt("targetFriend"))
            }.find { it.sourceFriend == sourceFriend && it.targetFriend == targetFriend }

            Log.d("isFollowing", relationship.toString())

            return relationship != null
        }

        fun toggleFollowingState(
            isFollowing: Boolean,
            sourceFriend: Int,
            targetFriend: Int,
            callback: (String) -> Unit
        ) {
            val json = JSONObject().apply {
                put("sourceFriend", sourceFriend)
                put("targetFriend", targetFriend)
            }

            Log.d("toggleFollowingState", json.toString())
            Log.d("toggleFollowingState (isFollowing)", "$isFollowing")

            if (isFollowing) {
                utils.post("followers/unfollow", json) {
                    callback(if (it) "Unfollowed" else "Error")
                }
            } else {
                utils.post("followers/follow", json) {
                    callback(if (it) "Followed" else "Error")
                }
            }
        }
    }
}
