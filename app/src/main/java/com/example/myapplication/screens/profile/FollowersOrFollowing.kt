package com.example.myapplication.screens.profile

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.myapplication.components.Tabs
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.UserToFollow
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState

@Composable
fun FollowersOrFollowing(navController: NavController) {
    var selectedTabIndex by remember {
        mutableStateOf(if (FollowersOrFollowingState.selected == "Followers") 0 else 1)
    }

    ProfileLayout(
        title = "johndoe54",
        onBackClick = { navController.navigate(Screens.UserProfile.route) }
    ) {
        Tabs(
            tabs = listOf("Followers", "Following"),
            selectedTabIndex = selectedTabIndex,
            onClick = { selectedTabIndex = it }
        ) {
            LazyColumn {
                items(100) {
                    UserToFollow(username = "User $it", navController)
                }
            }
        }
    }
}