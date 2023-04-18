package writenow.app.dbtables

import org.json.JSONObject
import java.time.LocalDate

data class Question(
    val id: Int,
    val text: String,
    val category: String,
    val author: Int,
    val dateRetrieved: Int? = null
)

class Questions private constructor() {
    companion object {
        private val utils = DBUtils("question")

        private suspend fun getAll(id: Int? = null) =
            utils.getAll(if (id == null) "" else "?id=$id") {
                Question(
                    id = it.getInt("QID"),
                    text = it.getString("QuestionText"),
                    category = it.getString("Category"),
                    author = it.getInt("Author"),
                )
            }

        /**
         * Gets a random question from the database.
         */
        suspend fun getRandom() = getAll().shuffled().first()
            .copy(dateRetrieved = LocalDate.now().dayOfMonth)

        fun add(question: String, category: String, author: String, callback: (Boolean) -> Unit) =
            utils.post("/add", JSONObject().apply {
                put("question", question)
                put("category", category)
                put("author", author)
            }, callback)
    }
}