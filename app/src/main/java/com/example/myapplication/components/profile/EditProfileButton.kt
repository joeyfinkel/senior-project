package com.example.myapplication.components.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.DefaultButton
import com.example.myapplication.screens.Screens

@Composable
fun EditProfileButton(
    modifier: Modifier = Modifier,
    borderRadius: Dp = 20.dp,
    navController: NavController
) {
    DefaultButton(
        modifier = modifier,
        width = 150.dp,
        spacedBy = 25.dp,
        btnText = "Edit profile",
        borderRadius = borderRadius,
        onBtnClick = { navController.navigate(Screens.EditProfile.route) }
    )
}