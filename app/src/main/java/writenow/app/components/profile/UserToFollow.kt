package writenow.app.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.dbtables.Follower
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@Composable
fun UserToFollow(follower: Follower, username: String, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    SelectedUserState.username = username
                    UserState.clickedFollower = true

                    navController.popBackStack()
                }
        ) {
            AccountCircle(size = 35.dp)
            Text(text = username, color = MaterialTheme.colorScheme.onSurface)
        }
        FollowOrUnFollow(follower = follower)
    }
}