package writenow.app.dbtables

import org.json.JSONObject

data class LikeAndComment(val postId: Int, val userId: Int, val liked: Int, val commentText: String)
data class LikedPost(val postId: Int, val userId: Int, val liked: Int)

class LikesAndComments private constructor() {
    companion object {
        private val utils = DBUtils("likeAndComment")

        suspend fun getAll(): List<LikeAndComment> {
            return utils.getAll {
                LikeAndComment(
                    it.getInt("postID"),
                    it.getInt("userID"),
                    it.getInt("isLike"),
                    it.getString("commentText"),
                )
            }
        }

        suspend fun getLikedPost(userId: Int, postId: Int): LikedPost? {
            val likesAndComments = getAll()
            val filtered = likesAndComments.filter { it.postId == postId && it.userId == userId }

            if (filtered.isEmpty()) {
                return null
            }

            return filtered.map {
                LikedPost(it.postId, it.userId, it.liked)
            }[0]
        }

        suspend fun didUserLikePost(postId: Int, userId: Int): Boolean {
            val likesAndComments = getAll()
            val filtered = likesAndComments.filter { it.postId == postId && it.userId == userId }

            return filtered.isNotEmpty() && filtered[0].liked == 1
        }

        fun toggleLikeState(isLiked: Boolean, jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            if (isLiked) {
                utils.put("like", jsonObject, callback)
            } else {
                utils.post("like", jsonObject, callback)
            }
        }

        fun comment(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("comment", jsonObject, callback)
        }
    }
}