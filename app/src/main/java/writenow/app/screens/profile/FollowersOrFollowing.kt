package writenow.app.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavController
import writenow.app.components.Tabs
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.UserToFollow
import writenow.app.dbtables.Follower
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@Composable
private fun FollowersList(list: MutableList<Follower>, navController: NavController) =
    list.forEach { follower ->
        UserToFollow(
            follower = follower, navController = navController
        )
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersOrFollowing(navController: NavController) {
    val (isTheSameUser) = remember { mutableStateOf(UserState.id == SelectedUserState.id) }
    val (selectedTabIndex, setSelectedTabIndex) = remember {
        mutableStateOf(if (UserState.followingOrFollower == "Followers") 0 else 1)
    }

    ProfileLayout(
        title = UserState.followingOrFollower,
        navController = navController,
        topVerticalArrangement = Arrangement.Center,
        onBackClick = {
            if (isTheSameUser) {
                navController.navigate(Screens.UserProfile)
            } else {
                if (UserState.clickedFollower) {
                    SelectedUserState.id = UserState.id

                    UserState.clickedFollower = false
                    navController.navigate(Screens.UserProfile)
                }

                navController.popBackStack()
            }
        },
        snackbar = null,
        content = { _, _, _ ->
            item {
                Tabs(
                    tabs = listOf("Followers", "Following"),
                    asPills = true,
                    selectedTabIndex = selectedTabIndex,
                    onClick = { index ->
                        setSelectedTabIndex(index)

                        UserState.followingOrFollower = if (index == 0) "Followers" else "Following"
                    }) {
                    when (UserState.followingOrFollower) {
                        "Followers" -> FollowersList(
                            list = if (isTheSameUser) UserState.followers else SelectedUserState.followers,
                            navController = navController
                        )
                        "Following" -> FollowersList(
                            list = if (isTheSameUser) UserState.following else SelectedUserState.following,
                            navController = navController
                        )
                    }
                }
            }

        })
}