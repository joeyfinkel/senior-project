package com.example.myapplication.components.bottomoverlay.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.icons.Like

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comment(fromUser: String, comment: String) {
    ListItem(
        modifier = Modifier
            .padding(PaddingValues(0.dp))
            .fillMaxWidth()
            .clickable {
                // TODO Reply to comment
                println("Replying to comment...")
            },
        text = { Text(text = fromUser) },
        secondaryText = { Text(text = comment) },
        icon = {
            AccountCircle(35.dp) {
                /* TODO Go to account
                     - Hide the BottomOverlay
                     - Show account page
                */
                println("Hello")
            }
        },
        trailing = { Like { println("You liked this comment") } }
    )
}