package com.example.myapplication.components.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.state.SelectedUserState
import com.example.myapplication.state.UserState

@Composable
fun UserToFollow(username: String, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .weight(1f)
                .clickable {
                    SelectedUserState.username = username
                    UserState.clickedFollower = true

                    navController.popBackStack()
                }
        ) {
            AccountCircle(size = 35.dp)
            Text(text = username)
        }
        ProfileButton(isEdit = true)
    }
}