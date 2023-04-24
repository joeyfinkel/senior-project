package writenow.app.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "report_reasons")
data class ReportReason(
    @PrimaryKey(autoGenerate = true) var id: Int, @ColumnInfo(name = "reason") val reason: String
)