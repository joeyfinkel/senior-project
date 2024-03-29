package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) var uuid: Int,
    @ColumnInfo(name = "firstName") var firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "displayName") val displayName: String?,
    @ColumnInfo(name = "bio") val bio: String?,
    @ColumnInfo(name = "active_days") val activeDays: String?,
    @ColumnInfo(name = "active_hours_start") val activeHoursStart: String?,
    @ColumnInfo(name = "active_hours_end") val activeHoursEnd: String?,
    @ColumnInfo(name = "has_posted") val hasPosted: Int?,
    @ColumnInfo(name = "is_post_private") val isPostPrivate: Int?
)