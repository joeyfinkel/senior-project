package writenow.app.components.profile

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.DefaultButton
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Relationship
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.utils.LaunchedEffectOnce

private var borderRadius = 20.dp

@Composable
fun EditProfile(navController: NavController) = DefaultButton(width = 100.dp,
    spacedBy = 25.dp,
    btnText = "Edit profile",
    borderRadius = borderRadius,
    onBtnClick = { navController.navigate(Screens.EditProfile) })

@Composable
fun FollowOrUnFollow(follower: Follower) {
    Log.d("ProfileButton", "follower: $follower")
    Log.d("ProfileButton", if (follower.isFollowing) "Following" else "Follow")

    var buttonText by remember { mutableStateOf("") }
    var isFollowing by remember { mutableStateOf(false) }
    var icon by remember { mutableStateOf<ImageVector?>(null) }

    LaunchedEffectOnce {
        isFollowing = follower.isFollowing
        buttonText = if (follower.isFollowing) "Following" else "Follow"
        icon = if (follower.isFollowing) Icons.Default.Check else Icons.Default.Add
    }

    fun toggleFollow() {
        Users.toggleFollowingState(
            isFollowing = isFollowing,
            relationship = Relationship(sourceFriend = UserState.id, targetFriend = follower.id),
        ) {
            when (it) {
                "Unfollowed" -> {
                    isFollowing = false
                    buttonText = "Follow"
                    icon = Icons.Default.Add
                }
                "Followed" -> {
                    isFollowing = true
                    buttonText = "Following"
                    icon = Icons.Default.Check
                }
            }
        }
    }

    DefaultButton(width = 100.dp,
        spacedBy = 25.dp,
        btnText = buttonText,
        icon = icon,
        borderRadius = borderRadius,
        onBtnClick = { toggleFollow() })
}