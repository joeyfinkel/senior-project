package writenow.app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import writenow.app.data.entity.Following

@Dao
abstract class FollowingDao {
    @Query("SELECT * FROM following")
    abstract suspend fun getFollowing(): List<Following>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFollowing(following: Following)
}