package writenow.app.components.profile

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.DefaultButton
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    borderRadius: Dp = 20.dp,
    navController: NavController? = null
) {
    var buttonText by remember { mutableStateOf("Follow") }
    var isFollowing by remember { mutableStateOf(false) }
    var icon by remember { mutableStateOf<ImageVector?>(null) }

    LaunchedEffect(Unit) {
        Log.d("isEdit", "$isEdit")
        if (!isEdit) {
            isFollowing = Users.isFollowing(UserState.id, SelectedUserState.userId.toInt())
            Log.d("isFollowing", "$isFollowing")
            buttonText = if (isFollowing) "Following" else "Follow"
            icon = if (isFollowing) Icons.Default.Check else Icons.Default.Add
        }
    }

    fun toggleFollow() {
        Users.toggleFollowingState(isFollowing, UserState.id, SelectedUserState.userId.toInt()) {
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

    DefaultButton(modifier = modifier,
        width = 100.dp,
        spacedBy = 25.dp,
        btnText = if (!isEdit) buttonText else "Edit profile",
        icon = if (!isEdit) icon else null,
        borderRadius = borderRadius,
        onBtnClick = {
            if (!isEdit && navController != null) navController.navigate(Screens.EditProfile)
            else toggleFollow()
        })
}