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
import com.example.myapplication.components.post.PostActions
import com.example.myapplication.components.profile.FollowButton
import com.example.myapplication.components.profile.Content
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.RowData
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState
import com.example.myapplication.state.SelectedUserState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun Profile(navController: NavController) {
    val username = SelectedUserState.username
    val userId = SelectedUserState.userId
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
                    FollowButton()
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
                    0 -> Content(count = 100) {
                        Post(
                            userId = if (userId.isEmpty()) 0 else userId.toInt(),
                            username = username,
                            navController = navController,
                            actionRow = {
                                PostActions(
                                    Modifier.align(Alignment.CenterHorizontally),
                                    state,
                                    scope
                                )
                            }
                        )
                    }
                    1 -> {
                        Text("Tab 2 Content")
                    }
                }
            }
        }
    }
}
