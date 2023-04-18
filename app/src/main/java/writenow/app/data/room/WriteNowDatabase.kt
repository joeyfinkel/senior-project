package writenow.app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import writenow.app.data.entity.Question
import writenow.app.data.entity.User

@Database(
    entities = [User::class, Question::class], version = 5, exportSchema = false
)
abstract class WriteNowDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
}