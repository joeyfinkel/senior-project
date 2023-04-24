package writenow.app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import writenow.app.data.entity.Follower

@Dao
abstract class FollowerDao {
    @Query("SELECT * FROM follower")
    abstract suspend fun getFollowers(): List<Follower>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertFollower(follower: Follower)
}