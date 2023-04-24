package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_post")
data class UserPost(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "visible") val visible: Int,
    @ColumnInfo(name = "created_at") var createdAt: String,
    @ColumnInfo(name = "is_edited") var isEdited: Boolean = false,
    @ColumnInfo(name = "is_liked") var isLiked: Boolean = false,
)
