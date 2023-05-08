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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import writenow.app.R
import writenow.app.components.Tabs
import writenow.app.components.post.LoadingPosts
import writenow.app.components.post.Post
import writenow.app.components.profile.EditProfile
import writenow.app.components.profile.FollowOrUnFollow
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.RowData
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Posts
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.GlobalState
import writenow.app.state.PostState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Profile(navController: NavController) {
    val (selectedTabIdx, setSelectedTabIdx) = remember { mutableStateOf(0) }
    var (isFollowing, setIsFollowing) = remember { mutableStateOf(false) }
    val (isTheSameUser, setIsTheSameUser) = remember { mutableStateOf(UserState.id == SelectedUserState.id) }
    val username =
        remember { if (isTheSameUser) UserState.username else SelectedUserState.username }
    val allPosts = remember { if (isTheSameUser) UserState.posts else mutableStateListOf() }
    val likedPosts = remember { if (isTheSameUser) UserState.likedPosts else mutableStateListOf() }

    LaunchedEffect(UserState.posts, UserState.likedPosts) {
        UserState.clickedFollower = !isTheSameUser

        setIsFollowing(Users.isFollowing(UserState.id, SelectedUserState.id!!))

        UserState.followers.addAll(GlobalState.followers?.map { Follower(it.id) }
            ?.toMutableList()!!)

        withContext(Dispatchers.IO) {
            if (UserState.id == SelectedUserState.id) {
                PostState.isLoading = true
                val usersPosts = Posts.getByUser(UserState.id)
                val usersLikedPosts = Posts.getLikedPosts(UserState.id)

                setIsTheSameUser(true)
                allPosts.clear()
                likedPosts.clear()

                if (UserState.posts != usersPosts) allPosts.addAll(usersPosts)
                if (UserState.likedPosts != usersLikedPosts) likedPosts.addAll(usersLikedPosts)
                PostState.isLoading = false
            } else {
                PostState.isLoading = true
                Log.d("Profile", "This is not the current user's profile page")
                setIsTheSameUser(false)
                allPosts.clear()
                likedPosts.clear()
                allPosts.addAll(Posts.getByUser(SelectedUserState.id!!).toMutableList())
                likedPosts.addAll(Posts.getLikedPosts(SelectedUserState.id!!).toMutableList())
                PostState.isLoading = false
            }
        }
    }

    LaunchedEffect(UserState.id != SelectedUserState.id) {
        isFollowing = Users.isFollowing(SelectedUserState.id!!, UserState.id)
    }

    ProfileLayout(
        title = SelectedUserState.displayName.ifEmpty { username },
        topText = "@${username.trim()}",
        topVerticalArrangement = Arrangement.spacedBy(10.dp),
        hasEllipsis = true,
        navController = navController,
        snackbar = null,
        onBackClick = {
//            if (UserState.clickedFollower) {
//                navController.navigate(Screens.FollowersOrFollowingList)
//                UserState.clickedFollower = false
//            } else {
//                navController.navigate(Screens.Posts)
//            }
            if (isTheSameUser) {
                navController.navigate(Screens.Posts)
            } else {
                navController.popBackStack()
            }
        },
        additionalTopContent = {
            if (isTheSameUser) {
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
                    RowData(primaryText = if (isTheSameUser) UserState.followers.size.toString() else SelectedUserState.followers.size.toString(),
                        secondaryText = "Followers",
                        onClick = {
                            UserState.followingOrFollower = "Followers"
                            navController.navigate(Screens.FollowersOrFollowingList)
                        })
                    RowData(primaryText = if (isTheSameUser) UserState.following.size.toString() else SelectedUserState.following.size.toString(),
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
        accountIconAction = null
    ) { state, scope, _ ->
        stickyHeader {
            Tabs(tabs = if (isTheSameUser) listOf(
                painterResource(id = R.drawable.grid_view), Icons.Default.Favorite
            )
            else listOf(painterResource(id = R.drawable.grid_view)),
                selectedTabIndex = selectedTabIdx,
                onClick = { setSelectedTabIdx(it) })
        }
        item {
            Column(
                modifier = Modifier.padding(top = 5.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (PostState.isLoading) {
                    LoadingPosts(postContainerHeight = DefaultWidth / 2, renderTotal = 3)
                } else {
                    when (selectedTabIdx) {
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
        }
    }
}

