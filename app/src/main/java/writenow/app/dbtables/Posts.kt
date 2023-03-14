package writenow.app.dbtables

import android.util.Log
import org.json.JSONObject

data class Post(
    val id: Int,
    val uuid: Int,
    val username: String,
    val text: String,
    val visible: Int,
    var createdAt: String
)

class Posts private constructor() {
    companion object {
        private val utils = DBUtils("post")

        suspend fun getAll(): List<Post> {
            return utils.getAll {
                Post(
                    it.getInt("postID"),
                    it.getInt("uuid"),
                    it.getString("username"),
                    it.getString("postContents"),
                    it.getInt("visible"),
                    it.getString("created")
                )
            }
        }

        suspend fun getLastPostDate(username: String): Int {
            val posts = getAll()
            val lastPost = posts.filter { it.username == username }.maxByOrNull { it.createdAt }!!
            val date = lastPost.createdAt.substringBefore(" ")
            Log.d("Posts", "getLastPostDate: $date")
            return date.substring(date.lastIndexOf("-") + 1).toInt()
        }

        fun post(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("add", jsonObject, callback)
        }
    }
}