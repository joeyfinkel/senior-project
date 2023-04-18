package writenow.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts

object PostState {
    /** Whether the posts are loading. */
    var isLoading by mutableStateOf(false)

    /**
     * Whether the new post is private.
     *
     * **Default is true.**
     * */
    var isPrivate by mutableStateOf(true)

    /** The list of all posts. */
    var allPosts = mutableListOf<Post>()

    /** The list of all deleted posts. */
    var deletedPosts = mutableListOf<Post>()

    /** The list of all liked posts. */
    var likedPosts = mutableListOf<Post>()

    suspend fun fetchNewPosts(hasPosted: Boolean): PostState {
        if (hasPosted) {
            isLoading = true
            allPosts = Posts.getToDisplay()
            isLoading = false
        } else {
            allPosts = Posts.getToDisplay()
        }

        return this
    }

    suspend fun updateAll(): PostState {
        deletedPosts = Posts.getDeleted(UserState.id).toMutableList()
        UserState.posts = Posts.getByUser(UserState.id).toMutableList()
        UserState.likedPosts = Posts.getLikedPosts(UserState.id).toMutableList()
        UserState.posts = Posts.getByUser(UserState.id).toMutableList()
        UserState.likedPosts = Posts.getLikedPosts(UserState.id).toMutableList()

        return this
    }
}