package com.example.myapplication.screens.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.R
import com.example.myapplication.components.Tabs
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.post.Post
import com.example.myapplication.components.profile.EditProfileButton
import com.example.myapplication.components.profile.FollowButton
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.RowData
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState
import com.example.myapplication.state.PostState
import com.example.myapplication.state.SelectedUserState
import com.example.myapplication.state.UserState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Profile(navController: NavController) {
    val username = SelectedUserState.username
    val posts = PostState.posts.filter { post -> post.username == username }
    var selectedTabIndex by remember { mutableStateOf(0) }

    ProfileLayout(
        title = username,
        onBackClick = { navController.navigate(Screens.Posts.route) },
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
                    AccountCircle(size = 75.dp)
                    Text(text = "@${username.trim()}")

                    if (username == UserState.username) {
                        EditProfileButton(navController = navController)
                    } else {
                        FollowButton()
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
                            RowData(
                                primaryText = "100",
                                secondaryText = "Followers",
                                onClick = {
                                    FollowersOrFollowingState.selected = "Followers"
                                    navController.navigate(Screens.FollowersOrFollowingList.route)
                                }
                            )
                            RowData(
                                primaryText = "90",
                                secondaryText = "Following",
                                onClick = {
                                    FollowersOrFollowingState.selected = "Following"
                                    navController.navigate(Screens.FollowersOrFollowingList.route)
                                }
                            )
                            RowData(primaryText = "45", secondaryText = "Likes")
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            stickyHeader {
                Tabs(
                    tabs = listOf(
                        painterResource(id = R.drawable.grid_view),
                        Icons.Default.Favorite
                    ),
                    selectedTabIndex = selectedTabIndex,
                    onClick = { index -> selectedTabIndex = index }
                )
            }
            item {
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
