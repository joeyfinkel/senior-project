package writenow.app.data.room

import androidx.room.*
import writenow.app.data.entity.User

@Dao
abstract class UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    abstract suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateUser(user: User)

    @Query("DELETE FROM users WHERE uuid = :uuid")
    abstract suspend fun deleteUser(uuid: Int)
}