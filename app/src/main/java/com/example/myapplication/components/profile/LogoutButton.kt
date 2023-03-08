package com.example.myapplication.components.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.state.UserState

@Composable
fun LogoutButton(navController: NavController, color: Color = Color.Black) {
    BottomOverlayButton(
        icon = painterResource(id = R.drawable.logout),
        text = "Logout",
        color = color,
        onClick = { UserState.logout(navController) }
    )
}