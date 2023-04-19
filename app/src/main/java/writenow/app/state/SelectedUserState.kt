package writenow.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import writenow.app.dbtables.Follower

/**
 * The global state of the user who [UserState] is currently viewing.
 */
object SelectedUserState {
    var id by mutableStateOf<Int?>(null)
    var username by mutableStateOf("")
    var displayName by mutableStateOf("")
    var birthday by mutableStateOf("")

    var followers = mutableListOf<Follower>()
    var following = mutableListOf<Follower>()
}