package writenow.app.data.repository

import writenow.app.data.entity.Comment
import writenow.app.data.room.CommentDao

class CommentRepository(private val commentDao: CommentDao) {
    suspend fun getComments(postId: Int) = commentDao.getCommentsByPostId(postId)

    suspend fun insertComment(comment: Comment) {
        commentDao.insertComment(comment)
    }
}