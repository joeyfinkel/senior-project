package writenow.app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import writenow.app.data.entity.Comment

@Dao
abstract class CommentDao {
    @Query("SELECT * FROM comment WHERE post_id = :postId")
    abstract suspend fun getCommentsByPostId(postId: Int): List<Comment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertComment(comment: Comment)
}