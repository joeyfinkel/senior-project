package writenow.app.data.repository

import writenow.app.data.entity.Question
import writenow.app.data.room.QuestionDao

class QuestionRepository(private val questionDao: QuestionDao) {
    suspend fun getQuestion(): Question? {
        val question = questionDao.getQuestion()

        if (question != null) {
            return Question(
                id = question.id,
                text = question.text,
                category = question.category,
                author = question.author,
                dateRetrieved = question.dateRetrieved
            )
        }

        return null
    }

    suspend fun addQuestion(question: Question) = questionDao.insertQuestion(
        Question(
            id = question.id,
            text = question.text,
            category = question.category,
            author = question.author,
            dateRetrieved = question.dateRetrieved
        )
    )


    suspend fun updateQuestion(question: Question) = questionDao.updateQuestion(
        Question(
            id = question.id,
            text = question.text,
            category = question.category,
            author = question.author,
            dateRetrieved = question.dateRetrieved
        )
    )


    suspend fun deleteQuestion(id: Int) = questionDao.deleteQuestion(id)

}
