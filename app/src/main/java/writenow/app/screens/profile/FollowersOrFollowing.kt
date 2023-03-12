package writenow.app.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import writenow.app.components.Tabs
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.UserToFollow
import writenow.app.screens.Screens
import writenow.app.state.FollowersOrFollowingState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersOrFollowing(navController: NavController) {
    var selectedTabIndex by remember {
        mutableStateOf(if (FollowersOrFollowingState.selected == "Followers") 0 else 1)
    }

    ProfileLayout(
        title = FollowersOrFollowingState.selected,
        navController = navController,
        onBackClick = {
            SelectedUserState.username = UserState.username

            navController.navigate(Screens.UserProfile)
        }
    ) { innerPadding, _, _ ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Tabs(
                tabs = listOf("Followers", "Following"),
                asPills = true,
                selectedTabIndex = selectedTabIndex,
                onClick = { index ->
                    selectedTabIndex = index
                    FollowersOrFollowingState.selected =
                        if (index == 0) "Followers" else "Following"
                }
            ) {
                LazyColumn {
                    items(100) { idx ->
                        UserToFollow(username = "User $idx", navController)
                    }
                }
            }
        }
    }
}