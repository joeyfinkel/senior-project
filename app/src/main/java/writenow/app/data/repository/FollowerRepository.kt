package writenow.app.data.repository

import android.util.Log
import writenow.app.data.entity.Follower
import writenow.app.data.room.FollowerDao

class FollowerRepository(private val followerDao: FollowerDao) {
    suspend fun getFollowers() = followerDao.getFollowers()
    suspend fun insertFollower(follower: Follower) {
        followerDao.insertFollower(follower)
        Log.d("FollowerRepository", "insertFollower: ${followerDao.getFollowers()}")
    }
}