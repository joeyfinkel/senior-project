package writenow.app.dbtables

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys.AES256_GCM_SPEC
import androidx.security.crypto.MasterKeys.getOrCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import writenow.app.state.UserState
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import javax.crypto.KeyGenerator

data class UserLoginInfo(val username: String, val password: String)
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
        private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
            name = "user_preferences"
        )

        private val utils = DBUtils("user")
        private val USERNAME = stringPreferencesKey("username")
        private val PASSWORD = stringPreferencesKey("password")
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

        suspend fun saveLoginInfo(context: Context, username: String, password: String) {
            withContext(Dispatchers.IO) {
                try {
                    // Create an instance of MasterKeys using the default constructor, which creates a new
                    // master key if one does not exist yet.
                    val masterKeyAlias = getOrCreate(AES256_GCM_SPEC)

                    // Create an instance of EncryptedSharedPreferences using the master key alias and
                    // the filename of the shared preferences file.
                    val sharedPreferences = EncryptedSharedPreferences.create(
                        SHARED_PREFS_FILENAME,
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )

                    // Generate a new AES key for encrypting the user's login information, and store it
                    // securely in the Android Keystore system.
                    val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES)
                    val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
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
                        putString("username", username)
                        putString("password", password)
                        apply()
                    }

                    Log.d("User", "Saved login info: $username, $password")
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: InvalidAlgorithmParameterException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }

        suspend fun isLoggedIn(context: Context): Boolean =
            context.userPreferencesDataStore.data.map {
                val username = it[USERNAME] ?: ""
                val password = it[PASSWORD] ?: ""

                UserLoginInfo(username, password)
                username.isNotEmpty() && password.isNotEmpty()
            }.first()

        suspend fun getUserLoginInfo(context: Context) {
            withContext(Dispatchers.IO) {
                try {
                    // Create an instance of MasterKeys using the default constructor, which creates a new
                    // master key if one does not exist yet.
                    val masterKeyAlias = getOrCreate(AES256_GCM_SPEC)

                    // Create an instance of EncryptedSharedPreferences using the master key alias and
                    // the filename of the shared preferences file.
                    val sharedPreferences = EncryptedSharedPreferences.create(
                        SHARED_PREFS_FILENAME,
                        masterKeyAlias,
                        context,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    )

                    // Retrieve the user's login information from the encrypted shared preferences file.
                    val username = sharedPreferences.getString("username", "")
                    val password = sharedPreferences.getString("password", "")

                    UserState.username = "$username"
                    UserState.password = "$password"

                    Log.d("User", "Retrieved login info: $username, $password")
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: InvalidAlgorithmParameterException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }
    }
}
