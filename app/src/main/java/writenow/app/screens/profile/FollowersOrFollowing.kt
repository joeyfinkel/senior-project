package writenow.app.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavController
import writenow.app.components.Tabs
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.UserToFollow
import writenow.app.dbtables.Follower
import writenow.app.dbtables.Users
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.utils.LaunchedEffectOnce

//@Composable
//private fun FollowersList(list: MutableList<Follower>, navController: NavController) = LazyColumn {
//    itemsIndexed(list) { _, follower ->
//        UserToFollow(
//            username = follower.id.toString(), navController = navController, follower = follower
//        )
//    }
//}

@Composable
private fun FollowersList(list: MutableList<Follower>, navController: NavController) =
    list.forEach { follower ->
        UserToFollow(
            username = follower.id.toString(), navController = navController, follower = follower
        )
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
        topVerticalArrangement = Arrangement.Center,
        onBackClick = { navController.popBackStack() },
        snackbar = null,
        content = { _, _, _ ->
            item {
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
                }
            }

        })
}