package writenow.app.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@Composable
fun UserToFollow(follower: Follower, navController: NavController) {
    val (isClicked, setIsClicked) = remember { mutableStateOf(false) }
    var selectedUsersPosts = remember { mutableListOf<Post>() }

    LaunchedEffect(isClicked) {
        if (isClicked) {
            val posts = Posts.getByUser(follower.id)

            selectedUsersPosts = posts.toMutableList()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    setIsClicked(true)

                    SelectedUserState.id = follower.id
                    SelectedUserState.username = follower.username
                    SelectedUserState.visitedProfiles.add(SelectedUserState.VisitedProfiles.apply {
                        id = follower.id
                        posts = selectedUsersPosts
                    })
                    UserState.clickedFollower = true

                    navController.navigate(Screens.UserProfile)
                }) {
            AccountCircle(size = 35.dp)
            Text(text = follower.username, color = MaterialTheme.colorScheme.onSurface)
        }
        FollowOrUnFollow(follower = follower)
    }
}