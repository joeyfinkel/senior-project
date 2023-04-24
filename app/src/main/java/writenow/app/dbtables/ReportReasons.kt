package writenow.app.dbtables

data class ReportReason(val id: Int, val reason: String)

class ReportReasons private constructor() {
    companion object {
        private val utils = DBUtils("post/report/reasons")

        suspend fun getAll(): List<ReportReason> {
            return utils.getAll {
                ReportReason(
                    id = it.getInt("reasonID"),
                    reason = it.getString("Reason")
                )
            }
        }
    }
}