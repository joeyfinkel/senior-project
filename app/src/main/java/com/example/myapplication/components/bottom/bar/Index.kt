package com.example.myapplication.components.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.bottom.bar.BaseIcon
import com.example.myapplication.screens.Screens
import com.example.myapplication.ui.theme.DefaultRadius
import com.example.myapplication.ui.theme.Primary

@Composable
fun BottomBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(
                color = Primary,
                shape = RoundedCornerShape(
                    topEnd = DefaultRadius,
                    topStart = DefaultRadius
                ),
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                screen = Screens.Posts.route
            )
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Search,
                selectedIcon = Icons.Filled.Search,
                screen = Screens.Search.route
            )
            BaseIcon(
                navController = navController,
                defaultIcon = Icons.Outlined.Notifications,
                selectedIcon = Icons.Filled.Notifications,
                screen = Screens.Notifications.route
            )
        }
    }
}