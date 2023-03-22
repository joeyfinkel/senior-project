package writenow.app.dbtables

import org.json.JSONObject

data class Comment(
    val id: Int,
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

class Comments private constructor() {
    companion object {
        private val utils = DBUtils("likeAndComment")

        fun post(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("add", jsonObject, callback)
        }
    }
}