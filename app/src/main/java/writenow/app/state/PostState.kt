package writenow.app.state

import writenow.app.dbtables.LikedPost
import writenow.app.dbtables.Post

object PostState {
    /** The list of all posts. */
    var allPosts = mutableListOf<Post>()

    /** The list of all liked posts. */
    var likedPosts = mutableListOf<LikedPost>()
}