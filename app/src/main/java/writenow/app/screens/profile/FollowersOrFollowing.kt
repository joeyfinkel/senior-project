package writenow.app.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import writenow.app.components.Tabs
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.UserToFollow
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Users
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.utils.LaunchedEffectOnce

@Composable
private fun FollowersList(list: MutableList<Follower>, navController: NavController) = LazyColumn {
    itemsIndexed(list) { _, follower ->
        UserToFollow(
            username = follower.id.toString(), navController = navController, follower = follower
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersOrFollowing(navController: NavController) {
    var selectedTabIndex by remember {
        mutableStateOf(if (UserState.followingOrFollower == "Followers") 0 else 1)
    }

    LaunchedEffectOnce {
        Users.updateRelationList(UserState.followers)
        Users.updateRelationList(UserState.following, false)
    }

    ProfileLayout(title = UserState.followingOrFollower,
        navController = navController,
        onBackClick = { navController.popBackStack() }) { innerPadding, _, _ ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Tabs(tabs = listOf("Followers", "Following"),
                asPills = true,
                selectedTabIndex = selectedTabIndex,
                onClick = { index ->
                    selectedTabIndex = index
                    UserState.followingOrFollower = if (index == 0) "Followers" else "Following"
                }) {
                when (UserState.followingOrFollower) {
                    "Followers" -> FollowersList(
                        list = if (SelectedUserState.id == UserState.id) UserState.followers else SelectedUserState.followers,
                        navController = navController
                    )
                    "Following" -> FollowersList(
                        list = if (SelectedUserState.id == UserState.id) UserState.following else SelectedUserState.following,
                        navController = navController
                    )
                }
//                LazyColumn {
//                    when (UserState.followingOrFollower) {
//                        "Followers" -> itemsIndexed(UserState.followers) { _, follower ->
//                            UserToFollow(
//                                username = follower.id.toString(),
//                                navController = navController,
//                                follower = follower
//                            )
//                        }
//                        "Following" -> itemsIndexed(UserState.following) { _, follower ->
//                            UserToFollow(
//                                username = follower.id.toString(),
//                                navController = navController,
//                                follower = follower
//                            )
//                        }
//                    }
//                }
            }
        }
    }
}