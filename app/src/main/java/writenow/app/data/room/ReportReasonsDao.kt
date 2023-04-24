package writenow.app.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import writenow.app.data.entity.ReportReason

@Dao
abstract class ReportReasonsDao {
    @Query("SELECT * FROM report_reasons")
    abstract suspend fun getReportReasons(): List<ReportReason>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertReportReasons(reportReasons: List<ReportReason>)
}