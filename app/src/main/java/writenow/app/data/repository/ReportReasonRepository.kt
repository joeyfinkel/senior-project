package writenow.app.data.repository

import writenow.app.data.entity.ReportReason
import writenow.app.data.room.ReportReasonsDao

class ReportReasonRepository(private val reportReasonsDao: ReportReasonsDao) {
    suspend fun getReportReasons(): List<ReportReason> {
        return reportReasonsDao.getReportReasons()
    }

    suspend fun insertReportReasons(reportReasons: List<ReportReason>) {
        reportReasonsDao.insertReportReasons(reportReasons)
    }
}