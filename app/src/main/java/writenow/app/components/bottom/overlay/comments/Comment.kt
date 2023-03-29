package writenow.app.components.bottom.overlay.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.dbtables.Comment
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.utils.getPostedDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun Comment(comment: Comment, navController: NavController) {
    ListItem(
        modifier = Modifier
            .padding(PaddingValues(0.dp))
            .fillMaxWidth()
            .clickable {
                // TODO Reply to comment
                println("Replying to comment...")
            },
        text = {
            Text(
                text = comment.username,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        secondaryText = {
            Column {
                Text(
                    text = comment.text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = getPostedDate(comment.dateCommented),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        icon = {
            AccountCircle(size = 35.dp) {
                SelectedUserState.id = comment.userId

                navController.navigate(Screens.UserProfile)
            }
        },
    )
}