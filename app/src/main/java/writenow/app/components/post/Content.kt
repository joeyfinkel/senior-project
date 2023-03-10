package writenow.app.components.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.utils.defaultText

/**
 * The content of the post. It will render out the who posted it and what they posted.
 * @param username The username of the user who posted.
 * @param text The contents of the post.
 */
@Composable
fun PostContent(userId: Int, username: String, text: String? = defaultText, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountCircle(
            size = 35.dp,
            modifier = Modifier.align(Alignment.Top)
        ) {
            SelectedUserState.userId = userId.toString()
            SelectedUserState.username = username

            navController.navigate(Screens.UserProfile)
        }
        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = username,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.width(5.dp))
            if (text != null) {
                Text(
                    text = text,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}