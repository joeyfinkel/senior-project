package writenow.app.state

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import writenow.app.data.entity.*
import writenow.app.data.repository.*
import writenow.app.data.room.WriteNowDatabase

@SuppressLint("StaticFieldLeak")
object GlobalState {
    lateinit var database: WriteNowDatabase
    lateinit var context: Context

    var user: User? = null
    var question: Question? = null
    var followers: List<Follower>? = null
    var followings: List<Following>? = null
    var reportReasons: List<ReportReason>? = null

    val isTheSameUser = SelectedUserState.id == UserState.id

    val userRepository by lazy { UserRepository(database.userDao()) }
    val questionRepository by lazy { QuestionRepository(database.questionDao()) }
    val reportReasonRepository by lazy { ReportReasonRepository(database.reportReasonsDao()) }
    val followingRepository by lazy { FollowingRepository(database.followingDao()) }
    val followerRepository by lazy { FollowerRepository(database.followerDao()) }
    val commentRepository by lazy { CommentRepository(database.commentDao()) }
    val userPostRepository by lazy { UserPostRepository(database.userPostDao()) }


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
        val migration5To6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `report_reasons` (
                        `id` INTEGER NOT NULL,
                        `reason` TEXT NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
            }
        }
        val migration6To7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `following` (
                        `id` INTEGER NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `follower` (
                        `id` INTEGER NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `comment` (
                        `id` INTEGER NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        `post_id` INTEGER NOT NULL,
                        `text` TEXT NOT NULL,
                        `is_deleted` INTEGER NULL,
                        `date_commented` TEXT NOT NULL,
                        `date_updated` TEXT NOT NULL,
                        `username` TEXT NOT NULL,
                        `is_liked` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `user_post` (
                        `id` INTEGER NOT NULL,
                        `user_id` INTEGER NOT NULL,
                        `username` TEXT NOT NULL,
                        `text` TEXT NOT NULL,
                        `visible` INTEGER NOT NULL,
                        `created_at` TEXT NOT NULL,
                        `is_edited` INTEGER NOT NULL,
                        `is_liked` INTEGER NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                """.trimIndent()
                )
            }
        }

        this.context = context
        database = Room.databaseBuilder(context, WriteNowDatabase::class.java, "writenow.db")
            .addMigrations(
                migration2To3, migration3To4, migration4To5, migration5To6, migration6To7
            ).build()

        runBlocking {
            user = userRepository.getUser()
            question = questionRepository.getQuestion()
            reportReasons = reportReasonRepository.getReportReasons()
            followers = followerRepository.getFollowers()
            Log.d("GlobalState", "followers: $followers")
            followings = followingRepository.getFollowing()

        }
    }
}