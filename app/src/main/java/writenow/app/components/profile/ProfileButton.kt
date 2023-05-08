package writenow.app.components.profile

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    val (buttonText, setButtonText) = remember { mutableStateOf("") }
    val (isFollowing, setIsFollowing) = remember { mutableStateOf(false) }
    val (icon, setIcon) = remember { mutableStateOf<ImageVector?>(null) }

    LaunchedEffectOnce {
        setIsFollowing(follower.isFollowing)
        setButtonText(if (follower.isFollowing) "Following" else "Follow")
        setIcon(if (follower.isFollowing) Icons.Default.Check else Icons.Default.Add)
    }

    fun toggleFollow() {
        Users.toggleFollowingState(
            isFollowing = isFollowing,
            relationship = Relationship(sourceFriend = UserState.id, targetFriend = follower.id),
        ) {
            when (it) {
                "Unfollowed" -> {
                    setIsFollowing(false)
                    setButtonText("Follow")
                    setIcon(Icons.Default.Add)
                }
                "Followed" -> {
                    setIsFollowing(true)
                    setButtonText("Following")
                    setIcon(Icons.Default.Check)
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