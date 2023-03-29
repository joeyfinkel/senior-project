package writenow.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import writenow.app.dbtables.Post

object PostState {
    /** Whether the posts are loading. */
    var isLoading by mutableStateOf(false)

    /** The list of all posts. */
    var allPosts = mutableListOf<Post>()

    /** The list of all liked posts. */
    var likedPosts = mutableListOf<Post>()
}