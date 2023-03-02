package com.example.myapplication.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myapplication.components.Tabs
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.UserToFollow
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState
import com.example.myapplication.state.SelectedUserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FollowersOrFollowing(navController: NavController) {
    var selectedTabIndex by remember {
        mutableStateOf(if (FollowersOrFollowingState.selected == "Followers") 0 else 1)
    }

    ProfileLayout(
        title = SelectedUserState.username,
        onBackClick = { navController.navigate(Screens.UserProfile.route) }
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
                onClick = { index -> selectedTabIndex = index }
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