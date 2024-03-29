package writenow.app.dbtables

import org.json.JSONObject
import writenow.app.state.UserState

data class PostLikes(
    val postId: Int, val userId: Int, val dateLiked: String = "", val isUnliked: Int
)

data class Post(
    /** The post's id */
    val id: Int,
    /** The user's id */
    val uuid: Int,
    val username: String,
    val text: String,
    val visible: Int,
    var createdAt: String,
    val question: String? = null,
    var isEdited: Boolean = false,
    var isLiked: Boolean = false,
    var comments: List<Comment> = emptyList(),
    var likes: List<PostLikes> = emptyList()
)

/** A private version of [Post] that is used to get data from the database. */
private data class PrivatePost(
    /** The post's id */
    val id: Int,
    /** The user's id */
    val uuid: Int,
    val username: String,
    val text: String,
    val visible: Int,
    var createdAt: String,
    var isLiked: Boolean = false,
    var comment: Comment? = null
)

class Posts private constructor() {
    companion object {
        private val utils = DBUtils("post")

        private suspend fun getAllWithComments(userId: Int? = null): List<Post> {
            val postsWithComments =
                utils.getAll("?withComments=true${if (userId != null) "&userID=$userId" else ""}") {
                    PrivatePost(
                        id = it.getInt("postID"),
                        uuid = it.getInt("userIdOfWhoPosted"),
                        username = it.getString("postedBy"),
                        text = it.getString("postContents"),
                        visible = it.getInt("isPostVisible"),
                        createdAt = it.getString("postedDate"),
                        comment = Comment(
                            id = it.getInt("commentID"),
                            userId = it.getInt("userWhoCommented"),
                            username = it.getString("commentedBy"),
                            postId = it.getInt("postID"),
                            text = it.getString("commentText"),
                            isDeleted = it.getInt("isCommentVisible"),
                            dateCommented = it.getString("dateCommented"),
                        )
                    )
                }

            return postsWithComments.groupBy { it.id to it.uuid }.map { (id, list) ->
                val comments = list.map { it.comment!! }
                Post(
                    id = id.first,
                    uuid = id.second,
                    username = list.first().username,
                    text = list.first().text,
                    visible = list.first().visible,
                    createdAt = list.first().createdAt,
                    isEdited = list.first().isLiked,
                    comments = comments,
                )
            }
        }

        private suspend fun getLikes(userId: Int? = null): List<PostLikes> {
            return utils.getAll("?withLikes=true${if (userId != null) "&userID=$userId" else ""}") {
                PostLikes(
                    it.getInt("postID"),
                    it.getInt("userID"),
                    it.getString("dateLiked"),
                    it.getInt("isUnliked")
                )
            }
        }

        /**
         * Gets all posts from the database with each posts comments and likes.
         *
         * @param userId The user's id.
         * @return A list of posts.
         */
        private suspend fun getAll(userId: Int? = null, isFeed: Boolean? = null): List<Post> {
            val id = if (userId != null) "userID=$userId" else ""
            val feed = if (isFeed != null) "isFeed=$isFeed" else ""
            val params = listOf(id, feed).filter { it.isNotEmpty() }.joinToString("&")
            val postsWithComments = getAllWithComments(userId)
            val allLikes = getLikes(userId)
            val allPosts = utils.getAll(if (params.isNotEmpty()) "?$params" else "") {
                Post(
                    id = it.getInt("postID"),
                    uuid = it.getInt("uuid"),
                    username = it.getString("username"),
                    text = it.getString("postContents"),
                    visible = it.getInt("visible"),
                    createdAt = it.getString("created"),
                    isLiked = allLikes.any { like ->
                        like.postId == it.getInt("postID") && like.userId == UserState.id && like.isUnliked == 0
                    },
                    isEdited = it.getInt("isEdited") == 1,
                    question = it.getString("QuestionText"),
                )
            }

            // Merge the posts with their comments and likes.
            return (postsWithComments + allPosts).distinctBy { it.id }.map { post ->
                val likes = allLikes.filter { it.postId == post.id }

                post.copy(likes = likes)
            }
        }

        suspend fun getLikesPerPost(postId: Int): Int {
            val likes = utils.getAll("?totalLikes=true&postID=$postId") {
                return@getAll it.getInt("totalLikes")
            }

            if (likes.isEmpty()) return 0

            return likes[0]
        }

        suspend fun isPostEdited(postId: Int): Boolean {
            val posts = utils.getAll("?postID=$postId") {
                return@getAll it.getInt("isEdited") == 1
            }

            if (posts.isEmpty()) return false

            return posts[0]
        }

        suspend fun getFeed(userId: Int) =
            getAll(userId, true).filter { it.visible == 1 && it.uuid != userId }
                .sortedByDescending { it.createdAt }.toMutableList()

        suspend fun getDiscover(userId: Int) =
            getAll().filter { it.visible == 1 && it.uuid != userId }
                .sortedByDescending { it.createdAt }.toMutableList()

        suspend fun getByUser(id: Int) = getAll(id).sortedByDescending { it.createdAt }

        suspend fun getDeleted(id: Int) = getByUser(id).filter { it.visible == 0 }

        suspend fun getLikedPosts(id: Int) = getByUser(id).filter { it.isLiked }

        suspend fun isLikedByUser(postId: Int, userId: Int): Boolean {
            val likes = getLikes()
            val postLikes = likes.filter { it.postId == postId }

            return postLikes.any { it.userId == userId }
        }

        /**
         * getLastPostDate
         * Returns the date of the most recent post made by the logged in user
         * Returns null if there are no posts made by the logged in user
         */
        suspend fun getLastPostDate(userId: Int): Int? {
            val posts = getAll().sortedByDescending { it.createdAt }
            //Log.d("Posts", posts.toString())
            // Check to see if there are posts from the user that is logged in
            if (!posts.filter { it.uuid == userId }.isNullOrEmpty()) {
                // Save the user's most recent post
                val lastPost = posts.filter { it.uuid == userId }[0]
                // Get the date of the user's last post
                val date = lastPost.createdAt.substringBefore(" ")
                // Return the date of the user's most recent post
                return date.substring(date.lastIndexOf("-") + 1).toInt()
            }
            // No posts from the logged in user, return null
            return null
        }

        suspend fun update(list: MutableList<Post>) {
            val posts = getAll()
            val newPosts = posts.toSet() - list.toSet()

            list.addAll(newPosts)
        }

        /**
         * Adds a post to the database.
         *
         * @param jsonObject The post to add.
         * @param callback A callback that is called when the post has been added.
         */
        fun post(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("add", jsonObject, callback)
        }

        fun edit(postId: Int, text: String, callback: (Boolean) -> Unit) {
            val json = JSONObject().put("postContents", text)

            utils.post("edit?postID=$postId", json, callback)
        }

        fun toggleLike(postId: Int, userId: Int, callback: (Boolean) -> Unit) {
            utils.post("like?postID=$postId&userID=$userId", callback)

        }

        fun delete(postId: Int, callback: (Boolean) -> Unit) {
            utils.post("delete?postID=$postId") {
                callback(it)
            }
        }

        fun comment(jsonObject: JSONObject, callback: (Boolean) -> Unit) {
            utils.post("comment/add", jsonObject, callback)
        }
    }
}