package writenow.app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import writenow.app.data.entity.UserPost

@Dao
abstract class UserPostDao {
    @Query("SELECT * FROM user_post")
    abstract suspend fun getPosts(): List<UserPost>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPost(post: UserPost)
}