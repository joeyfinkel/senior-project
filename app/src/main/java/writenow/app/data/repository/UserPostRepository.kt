package writenow.app.data.repository

import writenow.app.data.entity.UserPost
import writenow.app.data.room.UserPostDao

class UserPostRepository(private val userPostDao: UserPostDao) {
    suspend fun getPosts() = userPostDao.getPosts()
    suspend fun insertPost(post: UserPost) = userPostDao.insertPost(post)
}