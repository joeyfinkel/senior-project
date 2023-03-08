package com.example.myapplication.components.icons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun BaseAccountCircle(size: Dp) {
    Icon(
        Icons.Outlined.Person,
        contentDescription = "Test",
        modifier = Modifier.size(size),
        tint = Color.Black
    )
}

@Composable
fun AccountCircle(modifier: Modifier = Modifier, size: Dp = 50.dp, onClick: (() -> Unit)? = null) {
    BaseCircle(
        modifier = modifier,
        border = Modifier.border(2.dp, Color.Black, CircleShape),
        size = size,
        icon = { BaseAccountCircle(size) },
        onClick = onClick
    )
}