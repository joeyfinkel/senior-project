package writenow.app.state

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.runBlocking
import writenow.app.data.entity.User
import writenow.app.data.repository.UserRepository
import writenow.app.data.room.WriteNowDatabase

@SuppressLint("StaticFieldLeak")
object GlobalState {
    lateinit var database: WriteNowDatabase
    lateinit var context: Context

    var user: User? = null

    val userRepository by lazy { UserRepository(database.userDao()) }

    @SuppressLint("StaticFieldLeak")
    fun provide(context: Context) {
//        val migration = object : Migration(3, 4) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE users ADD COLUMN bitmap BLOB")
//            }
//        }
        this.context = context
        database =
            Room.databaseBuilder(context, WriteNowDatabase::class.java, "writenow.db").build()

        runBlocking {
            user = userRepository.getUser()
        }
    }
}