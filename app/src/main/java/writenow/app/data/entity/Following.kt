package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "following", primaryKeys = ["id"])
data class Following(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "user_id") val userId: Int
)
