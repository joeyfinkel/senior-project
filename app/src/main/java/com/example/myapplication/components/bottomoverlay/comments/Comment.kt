package com.example.myapplication.components.bottomoverlay.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.icons.Like
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.SelectedUserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comment(userId: Int, username: String, comment: String, navController: NavController) {
    ListItem(
        modifier = Modifier
            .padding(PaddingValues(0.dp))
            .fillMaxWidth()
            .clickable {
                // TODO Reply to comment
                println("Replying to comment...")
            },
        text = { Text(text = username) },
        secondaryText = { Text(text = comment) },
        icon = {
            AccountCircle(size = 35.dp) {
                SelectedUserState.userId = userId.toString()

                navController.navigate(Screens.UserProfile.route)
            }
        },
        trailing = { Like { println("You liked this comment") } }
    )
}