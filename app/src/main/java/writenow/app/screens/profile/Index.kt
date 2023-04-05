package writenow.app.screens.profile

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.Tabs
import writenow.app.components.post.Post
import writenow.app.components.profile.EditProfile
import writenow.app.components.profile.FollowOrUnFollow
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.RowData
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Posts
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.utils.LaunchedEffectOnce

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Profile(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var isFollowing by remember { mutableStateOf(false) }

    val username = SelectedUserState.username
    val allPosts = remember { UserState.posts.filter { it.visible == 1 }.toMutableList() }
    val likedPosts = remember { UserState.likedPosts.filter { it.visible == 1 }.toMutableList() }

    LaunchedEffectOnce {
        isFollowing = Users.isFollowing(UserState.id, SelectedUserState.id!!)

        Log.d("Profile", "isFollowing: $isFollowing")

        if (UserState.id == SelectedUserState.id) {
            allPosts.addAll(Posts.getByUser(SelectedUserState.id!!))
            likedPosts.addAll(Posts.getLikedPosts(SelectedUserState.id!!))
        }
    }

    LaunchedEffect(UserState.id != SelectedUserState.id) {
        isFollowing = Users.isFollowing(SelectedUserState.id!!, UserState.id)

        Users.updateRelationList(SelectedUserState.followers)
        Users.updateRelationList(SelectedUserState.following, false)
    }

    ProfileLayout(title = SelectedUserState.displayName.ifEmpty { username },
        topText = "@${username.trim()}",
        topVerticalArrangement = Arrangement.spacedBy(10.dp),
        hasEllipsis = true,
        navController = navController,
        onBackClick = {
            if (UserState.clickedFollower) {
                navController.navigate(Screens.FollowersOrFollowingList)
                UserState.clickedFollower = false
            } else {
                navController.navigate(Screens.Posts)
            }
        },
        additionalTopContent = {
            if (username == UserState.username) {
                // This is the current user's profile
                EditProfile(navController = navController)
            } else {
                // This is the selected user's profile
                FollowOrUnFollow(
                    follower = Follower(
                        id = SelectedUserState.id!!, isFollowing = isFollowing
                    )
                )
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
                    RowData(primaryText = if (SelectedUserState.id == UserState.id) UserState.followers.size.toString() else SelectedUserState.followers.size.toString(),
                        secondaryText = "Followers",
                        onClick = {
                            UserState.followingOrFollower = "Followers"
                            navController.navigate(Screens.FollowersOrFollowingList)
                        })
                    RowData(primaryText = if (SelectedUserState.id == UserState.id) UserState.following.size.toString() else SelectedUserState.following.size.toString(),
                        secondaryText = "Following",
                        onClick = {
                            UserState.followingOrFollower = "Following"
                            navController.navigate(Screens.FollowersOrFollowingList)
                        })
                    RowData(primaryText = "45", secondaryText = "Likes")
                }
                if (UserState.bio.isNotBlank() || UserState.bio.isNotEmpty()) Text(text = UserState.bio)
            }
            Spacer(modifier = Modifier.height(15.dp))
        },
        content = { state, scope ->
            stickyHeader {
                Tabs(tabs = if (SelectedUserState.username == UserState.username) listOf(
                    painterResource(id = R.drawable.grid_view), Icons.Default.Favorite
                )
                else listOf(painterResource(id = R.drawable.grid_view)),
                    selectedTabIndex = selectedTabIndex,
                    onClick = { index -> selectedTabIndex = index })
            }
            item {
                Column(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (selectedTabIndex) {
                        0 -> allPosts.forEach { post ->
                            Post(
                                post = post,
                                navController = navController,
                                state = state,
                                coroutineScope = scope
                            )
                        }
                        1 -> likedPosts.forEach { post ->
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
        })
}

