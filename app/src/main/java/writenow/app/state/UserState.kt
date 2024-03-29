package writenow.app.state

import android.graphics.Bitmap
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.dbtables.*
import writenow.app.screens.Screens
import writenow.app.utils.ActiveHours
import java.time.LocalDate


/**
 * The global state of the user.
 */
object UserState {
    var id by mutableStateOf(0)

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var username by mutableStateOf("")
    var birthday by mutableStateOf("")
    var displayName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var bio by mutableStateOf("")
    var followingOrFollower by mutableStateOf("")
    var token by mutableStateOf("")

    var isPostPrivate by mutableStateOf(false)
    var isLoggedIn by mutableStateOf(false)
    var isCommentClicked by mutableStateOf(false)
    var isEllipsisClicked by mutableStateOf(false)
    var isProfileEllipsisClicked by mutableStateOf(false)
    var isPostClicked by mutableStateOf(false)
    var isInDiscoverMode by mutableStateOf(false)
    var clickedFollower by mutableStateOf(false)
    var clickedOnSettings by mutableStateOf(false)
    var hasPosted by mutableStateOf(false)
    var hasClickedLogOut by mutableStateOf(false)

    var selectedPost by mutableStateOf<Post?>(null)
    var bitmap by mutableStateOf<Bitmap?>(null)
    var currentQuestion by mutableStateOf<Question?>(null)

    var selectedDays by mutableStateOf(setOf<String>())
    var activeHours by mutableStateOf(ActiveHours("", ""))

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
        hasPosted = date == Posts.getLastPostDate(userId = id)

        return hasPosted
    }

    suspend fun updateActiveHours(userId: Int) {
        val prefs = Users.getPrefs(userId)

        if (prefs.isNotEmpty()) activeHours = prefs[0].activeHours
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

    @OptIn(ExperimentalMaterialApi::class)
    fun editPost(
        navController: NavController,
        scope: CoroutineScope,
        sheetState: ModalBottomSheetState
    ) {
        navController.navigate(Screens.EditPost)

        scope.launch {
            sheetState.hide()

            isEllipsisClicked = false
            isCommentClicked = false
            isPostClicked = false
        }
    }
}
