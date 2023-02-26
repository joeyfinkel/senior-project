package com.example.myapplication.components.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.DefaultButton

@Composable
fun FollowButton(modifier: Modifier = Modifier, borderRadius: Dp = 20.dp) {
    var buttonText by remember { mutableStateOf("Follow") }

    DefaultButton(
        modifier = modifier,
        width= 150.dp,
        spacedBy = 25.dp,
        btnText = buttonText,
        icon = if (buttonText == "Follow") Icons.Default.Add else Icons.Default.Check,
        borderRadius = borderRadius,
        onBtnClick = {
            buttonText = if (buttonText == "Follow") "Following" else "Follow"
        }
    )
}