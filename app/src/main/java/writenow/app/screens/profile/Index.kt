package writenow.app.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.Tabs
import writenow.app.components.icons.AccountCircle
import writenow.app.components.post.Post
import writenow.app.components.profile.ProfileButton
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.RowData
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.FollowersOrFollowingState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Profile(navController: NavController) {
    val username = SelectedUserState.username
    var selectedTabIndex by remember { mutableStateOf(0) }
    val posts = remember { mutableStateListOf<Post>() }


    LaunchedEffect(Unit) {
        posts.addAll(Posts.getByUsername(username))
    }

    ProfileLayout(
        /* Display name at the top*/
        title = SelectedUserState.displayName.ifEmpty { username },
        navController = navController,
        onBackClick = {
            if (UserState.clickedFollower) {
                navController.navigate(Screens.FollowersOrFollowingList)
                UserState.clickedFollower = false
            } else {
                navController.navigate(Screens.Posts)
            }
        },
        hasEllipsis = true
    ) { innerPadding, state, scope ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    // Profile Picture
                    AccountCircle(size = 75.dp)
                    Text(text = "@${username.trim()}", color = MaterialTheme.colorScheme.onSurface)

                    if (username == UserState.username) {
                        ProfileButton(navController = navController, isEdit = true)
                    } else {
                        ProfileButton()
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(0.75f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RowData(primaryText = "100", secondaryText = "Followers", onClick = {
                                FollowersOrFollowingState.selected = "Followers"
                                navController.navigate(Screens.FollowersOrFollowingList)
                            })
                            RowData(primaryText = "90", secondaryText = "Following", onClick = {
                                FollowersOrFollowingState.selected = "Following"
                                navController.navigate(Screens.FollowersOrFollowingList)
                            })
                            RowData(primaryText = "45", secondaryText = "Likes")
                        }
                        if (UserState.bio.isNotBlank() || UserState.bio.isNotEmpty()) Text(text = UserState.bio)
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            stickyHeader {
                Tabs(
                    tabs = if (SelectedUserState.username == UserState.username)
                        listOf(painterResource(id = R.drawable.grid_view), Icons.Default.Favorite)
                    else listOf(painterResource(id = R.drawable.grid_view)),
                    selectedTabIndex = selectedTabIndex,
                    onClick = { index -> selectedTabIndex = index }
                )
            }
            item {
                Column(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> posts.forEach { post ->
                            Post(
                                post = post,
                                navController = navController,
                                state = state,
                                coroutineScope = scope
                            )
                        }
                        1 -> UserState.likedPosts.forEach { post ->
                            Post(
                                post = post,
                                navController = navController,
                                state = state,
                                coroutineScope = scope
                            )
                        }
                    }
                }
            }
        }
    }
}
