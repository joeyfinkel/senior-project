package writenow.app.dbtables

import android.util.Log
import org.json.JSONObject

data class Post(
    /** The post's id */
    val id: Int,
    /** The user's id */
    val uuid: Int,
    val username: String,
    val text: String,
    val visible: Int,
    var createdAt: String,
    var isLiked: Boolean = false,
) {
    fun toggleLikeState() {
        isLiked = !isLiked
    }
}

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

        suspend fun getByUsername(username: String): List<Post> {
            return getAll().filter { it.username == username }
        }

        suspend fun getLastPostDate(username: String): Int? {
            Log.d("username", username)
            if (username == "") return null

            val posts = getAll()

            if (posts.isNotEmpty()) {
                val lastPost =
                    posts.filter { it.username == username }.maxByOrNull { it.createdAt }!!
                val date = lastPost.createdAt.substringBefore(" ")

                return date.substring(date.lastIndexOf("-") + 1).toInt()
            }

            return null
        }

        fun post(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("add", jsonObject, callback)
        }
    }
}