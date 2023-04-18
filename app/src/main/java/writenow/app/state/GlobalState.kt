package writenow.app.state

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import writenow.app.data.entity.Question
import writenow.app.data.entity.User
import writenow.app.data.repository.QuestionRepository
import writenow.app.data.repository.UserRepository
import writenow.app.data.room.WriteNowDatabase

@SuppressLint("StaticFieldLeak")
object GlobalState {
    lateinit var database: WriteNowDatabase
    lateinit var context: Context

    var user: User? = null
    var question: Question? = null

    val userRepository by lazy { UserRepository(database.userDao()) }
    val questionRepository by lazy { QuestionRepository(database.questionDao()) }

    @SuppressLint("StaticFieldLeak")
    fun provide(context: Context) {
        val migration2To3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `questions` (
                        `id` INTEGER NOT NULL,
                        `text` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `author` INTEGER NOT NULL,
                        `date_retrieved` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
            }
        }
        val migration3To4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN has_posted INTEGER NULL")
            }
        }
        val migration4To5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE users ADD COLUMN is_post_private INTEGER NULL")
            }
        }
        this.context = context
        database =
            Room.databaseBuilder(context, WriteNowDatabase::class.java, "writenow.db")
                .addMigrations(migration2To3, migration3To4, migration4To5)
                .build()

        runBlocking {
            user = userRepository.getUser()
        }
    }
}