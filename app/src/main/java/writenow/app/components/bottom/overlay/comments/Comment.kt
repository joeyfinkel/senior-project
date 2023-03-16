package writenow.app.components.bottom.overlay.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Comment(userId: Int, username: String, comment: String, navController: NavController) {
    ListItem(
        modifier = Modifier
            .padding(PaddingValues(0.dp))
            .fillMaxWidth()
            .clickable {
                // TODO Reply to comment
                println("Replying to comment...")
            },
        text = { Text(text = username) },
        secondaryText = { Text(text = comment) },
        icon = {
            AccountCircle(size = 35.dp) {
                SelectedUserState.userId = userId.toString()

                navController.navigate(Screens.UserProfile)
            }
        },
//        trailing = { Like { println("You liked this comment") } }
    )
}