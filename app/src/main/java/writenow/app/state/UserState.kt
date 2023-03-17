package writenow.app.state

import android.annotation.SuppressLint
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.navigation.NavController
import writenow.app.dbtables.Post
import writenow.app.screens.Screens

/**
 * The global state of the user.
 */
object UserState {
    var id by mutableStateOf(0)
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var displayName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var bio by mutableStateOf("")
    var isLoggedIn by mutableStateOf(false)
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
    var clickedFollower by mutableStateOf(false)
    var hasPosted by mutableStateOf(false)
    var selectedPost by mutableStateOf<Post?>(null)

    @SuppressLint("CompositionLocalNaming")
    @OptIn(ExperimentalMaterialApi::class)
    var selectedPostState = compositionLocalOf<ModalBottomSheetState> {
        error("No ModalBottomSheetState found!")
    }
    var posts = mutableListOf<Post>()
    var likedPosts = mutableListOf<Post>()

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

    fun logout(navController: NavController) {
        navController.navigate(Screens.MainScreen)
        reset()
    }
}
