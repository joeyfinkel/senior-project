package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "followers")
data class Followers(@ColumnInfo(name = "id") val followerId: Int)