package writenow.app.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import writenow.app.data.entity.*

@Database(
    entities = [User::class, Question::class, ReportReason::class, Following::class, Follower::class, Comment::class, UserPost::class],
    version = 7,
    exportSchema = false
)
abstract class WriteNowDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questionDao(): QuestionDao
    abstract fun reportReasonsDao(): ReportReasonsDao
    abstract fun followingDao(): FollowingDao
    abstract fun followerDao(): FollowerDao
    abstract fun commentDao(): CommentDao
    abstract fun userPostDao(): UserPostDao
}