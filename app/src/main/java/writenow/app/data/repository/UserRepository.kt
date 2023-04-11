package writenow.app.data.repository

import writenow.app.data.entity.User
import writenow.app.data.room.UserDao
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec

class UserRepository(private val userDao: UserDao) {
    suspend fun getUser(): User? {
        val user = userDao.getUser()

        if (user != null) {
            val keyGenerator = KeyGenerator.getInstance("AES").apply {
                init(256)
            }
            val key = keyGenerator.generateKey()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
                if (iv == null) {
                    init(Cipher.ENCRYPT_MODE, key)
                } else {
                    val ivSpec = IvParameterSpec(iv)
                    init(Cipher.DECRYPT_MODE, key, ivSpec)
                }
            }
            val decryptedPassword = cipher.doFinal(user.password.toByteArray())

            return User(
                uuid = user.uuid,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                password = decryptedPassword.toString(),
                username = user.username,
                displayName = user.displayName,
                bio = user.bio,
                activeDays = user.activeDays,
                activeHoursStart = user.activeHoursStart,
                activeHoursEnd = user.activeHoursEnd,
            )
        }

        return null
    }

    suspend fun addUser(user: User) {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(256)
        val key = keyGenerator.generateKey()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedPassword = cipher.doFinal(user.password.toByteArray())

        userDao.insertUser(
            User(
                uuid = user.uuid,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                password = encryptedPassword.toString(),
                username = user.username,
                displayName = user.displayName,
                bio = user.bio,
                activeDays = user.activeDays,
                activeHoursStart = user.activeHoursStart,
                activeHoursEnd = user.activeHoursEnd,
            )
        )
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.uuid)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}