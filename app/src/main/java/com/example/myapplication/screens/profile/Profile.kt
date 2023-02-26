package com.example.myapplication.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.myapplication.components.profile.FollowButton
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.RowData
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState

@Composable
fun Profile(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var buttonText by remember { mutableStateOf("Follow") }

    ProfileLayout(
        title = "John Doe",
        onBackClick = { navController.navigate(Screens.Posts.route) },
        hasEllipsis = true,
        additionalTopContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                AccountCircle(size = 75.dp)
                Text(text = "@johndoe54")
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
                    Spacer(modifier = Modifier.height(14.dp))
                    FollowButton()
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    ) {
        Column {
            Tabs(
                tabs = listOf(painterResource(id = R.drawable.grid_view), Icons.Default.Favorite),
                selectedTabIndex = selectedTabIndex,
                onClick = { selectedTabIndex = it }
            ) {
                LazyColumn {
                    items(1) {
                        when (selectedTabIndex) {
                            0 -> {
                                Text("Tab 1 Content")
                            }
                            1 -> {
                                Text("Tab 2 Content")
                            }
                        }

                    }
                }
            }
        }
    }
}
