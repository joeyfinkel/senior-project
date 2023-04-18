package writenow.app.data.room

import androidx.room.*
import writenow.app.data.entity.Question

@Dao
abstract class QuestionDao {
    @Query("SELECT * FROM questions LIMIT 1")
    abstract suspend fun getQuestion(): Question?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertQuestion(question: Question)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun updateQuestion(question: Question)

    @Query("DELETE FROM questions WHERE id = :id")
    abstract suspend fun deleteQuestion(id: Int)
}