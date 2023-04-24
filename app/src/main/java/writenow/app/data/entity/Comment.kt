package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "post_id") val postId: Int,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "is_deleted") val isDeleted: Int? = null,
    @ColumnInfo(name = "date_commented") var dateCommented: String = "",
    @ColumnInfo(name = "date_updated") var dateUpdated: String = "",
    @ColumnInfo(name = "username") val username: String = "",
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
)
