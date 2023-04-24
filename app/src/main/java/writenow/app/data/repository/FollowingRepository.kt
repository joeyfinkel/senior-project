package writenow.app.data.repository

import writenow.app.data.entity.Following
import writenow.app.data.room.FollowingDao

class FollowingRepository(private val followingDao: FollowingDao) {
    suspend fun getFollowing() = followingDao.getFollowing()
    suspend fun insertFollowing(following: Following) = followingDao.insertFollowing(following)
}