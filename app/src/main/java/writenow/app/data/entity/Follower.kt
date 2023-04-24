package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "follower", primaryKeys = ["id"])
data class Follower(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int
)