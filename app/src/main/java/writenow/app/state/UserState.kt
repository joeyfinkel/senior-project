package writenow.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Post
import writenow.app.screens.Screens
import java.time.LocalDate

/**
 * The global state of the user who is logged in.
 */
object UserState {
    /** The id of the user that is logged in. */
    var id by mutableStateOf(0)

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var displayName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var bio by mutableStateOf("")
    var followingOrFollower by mutableStateOf("Followers")

    var isLoggedIn by mutableStateOf(false)
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
    var clickedFollower by mutableStateOf(false)
    var isPostClicked by mutableStateOf(false)
    var hasPosted by mutableStateOf(false)
    var hasClickedLogOut by mutableStateOf(false)

    var selectedPost by mutableStateOf<Post?>(null)

    var posts = mutableListOf<Post>()
    var likedPosts = mutableListOf<Post>()
    var followers = mutableListOf<Follower>()
    var following = mutableListOf<Follower>()

    operator fun get(username: String): Any {
        return when (username) {
            "firstName" -> firstName
            "lastName" -> lastName
            "email" -> email
            "passwordHash" -> password
            "username" -> username
            else -> ""
        }
    }

    suspend fun getHasPosted(): Boolean {
        val date = LocalDate.now().dayOfMonth
//        hasPosted = date == Posts.getLastPostDate(username)

        return true
    }

    /**
     * Resets the user state.
     */
    private fun reset() {
        id = 0
        firstName = ""
        lastName = ""
        username = ""
        displayName = ""
        email = ""
        password = ""
        bio = ""
        isLoggedIn = false
        isCommentClicked = false
        isEllipsisClicked = false
        likedPosts = mutableListOf()
        clickedFollower = false
    }

    /**
     * Navigate to the login screen and resets the user state.
     */
    fun logout(navController: NavController) {
        navController.navigate(Screens.MainScreen)
        reset()
    }
}
