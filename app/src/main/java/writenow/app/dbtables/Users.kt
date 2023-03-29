package writenow.app.dbtables

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.security.crypto.MasterKey.Builder
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import writenow.app.state.PostState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator

data class Follower(val id: Int, val isFollowing: Boolean = false)
data class Relationship(val sourceFriend: Int, val targetFriend: Int)
data class User(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val passwordHash: String,
    val followingList: MutableList<Int> = mutableListOf(),
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

        /**
         * Gets a list of user ids that the user with the given id is following.
         *
         * @param id The id of the user to get the list of users that they are following.
         * @return A list of user ids that the user with the given [id] is following.
         */
        private suspend fun getFollowing(id: Int): List<Follower> {
            return utils.getAll("relationship/following?sourceFriend=$id") {
                Follower(it.getInt("targetFriend"))
            }
        }

        /**
         * Gets a list of user ids that are following the current user.
         *
         * @param id The id of the user to get the list of users that are following them.
         * @return A list of user ids that are following the user with the given [id].
         */
        private suspend fun getFollowers(id: Int): List<Follower> {
            return utils.getAll("relationship/followers?targetFriend=$id") {
                Follower(it.getInt("sourceFriend"))
            }
        }

        /**
         * Uses the [getFollowing] function to get a list of users that the user is following and
         * adds them to [list] if they are not already in the list.
         *
         * @param list The list to update with the new followers.
         * **This should be [UserState.followers]**.
         * @param getFollowers If true, the function will get the followers of the user
         * instead of the users that the user is following.
         */
        suspend fun updateRelationList(list: MutableList<Follower>, getFollowers: Boolean = true) {
            if (SelectedUserState.id != null) {
                val id =
                    if (UserState.id == SelectedUserState.id) UserState.id else SelectedUserState.id
                Log.d("updateRelationList", "id: $id, getFollowers: $getFollowers")
                val followers = if (getFollowers) getFollowers(id!!).map {
                    Follower(it.id, isFollowing = isFollowing(id, it.id))
                } else getFollowing(UserState.id).map {
                    Follower(it.id, isFollowing = isFollowing(UserState.id, it.id))
                }
                val newFollowers = followers.toSet() - list.toSet()

                list.addAll(newFollowers)
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

        suspend fun saveInfo(context: Context, user: UserState) {
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
                        putString("allPosts", Gson().toJson(PostState.allPosts))
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

        suspend fun getInfo(context: Context) {
            withContext(Dispatchers.IO) {
                try {
                    val sharedPreferences = getSharedPreferences(context)
                    val id = sharedPreferences.getString("id", "")
                    val username = sharedPreferences.getString("username", "")
                    val password = sharedPreferences.getString("password", "")
                    val allPosts = sharedPreferences.getString("allPosts", "")

                    Log.d("Username", "$username")

                    if (username != null && password != null) {
                        UserState.username = "$username"
                        UserState.password = "$password"
                        UserState.isLoggedIn = true

                        Log.d("allPosts", allPosts.toString())

//                        if (allPosts != null) PostState.allPosts =
//                            Gson().fromJson(allPosts, Array<Post>::class.java).toMutableList()


                        if (id != null && id.isNotBlank()) {
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

        suspend fun clearInfo(context: Context) {
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
         * Checks if [sourceFriend] is following [targetFriend].
         *
         * @param sourceFriend The current user's ID.
         * @param targetFriend The selected user's ID.
         * @return Boolean The boolean value of whether the current user is following the selected user.
         */
        suspend fun isFollowing(sourceFriend: Int, targetFriend: Int): Boolean {
            val relationship = utils.getAll("followers") {
                Relationship(it.getInt("sourceFriend"), it.getInt("targetFriend"))
            }.find { it.sourceFriend == sourceFriend && it.targetFriend == targetFriend }

            Log.d("isFollowing", relationship.toString())

            if (relationship != null) {
                Log.d("isFollowing", "true")
            } else {
                Log.d("isFollowing", "false")
            }

            return relationship != null
        }

        fun toggleFollowingState(
            isFollowing: Boolean, relationship: Relationship, callback: (String) -> Unit
        ) {
            val json = JSONObject().apply {
                put("sourceFriend", relationship.sourceFriend)
                put("targetFriend", relationship.targetFriend)
            }

            Log.d("toggleFollowingState", json.toString())
            Log.d("toggleFollowingState (isFollowing)", "$isFollowing")

            if (isFollowing) {
                utils.delete("relationship/unfollow", json) {
                    callback(if (it) "Unfollowed" else "Error")
                }
            } else {
                utils.post("relationship/follow", json) {
                    callback(if (it) "Followed" else "Error")
                }
            }
        }
    }
}
