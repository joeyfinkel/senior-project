package com.example.myapplication.screens.profile

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
import com.example.myapplication.components.profile.*
import com.example.myapplication.components.profile.Content
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.FollowersOrFollowingState
import com.example.myapplication.state.SelectedUserState
import com.example.myapplication.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfile(navController: NavController) {
    val username = SelectedUserState.username
    val userId = SelectedUserState.userId
    var selectedTabIndex by remember { mutableStateOf(0) }

    ProfileLayout(
        title = username,
        onBackClick = { navController.popBackStack() },
        hasEllipsis = true
    ) { innerPadding, state, scope ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            AccountCircle(size = 75.dp)
            Text(text = "@${username.trim()}")
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}