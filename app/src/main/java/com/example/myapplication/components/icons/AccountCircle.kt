package com.example.myapplication.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBar

@Composable
fun AccountCircle(size: Dp = 50.dp, modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = modifier) {
        Box(
            modifier = Modifier
                .background(AppBar, shape = CircleShape)
                .border(
                    2.dp, Color.Black, CircleShape
                )
                .size(size)
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Test",
                modifier = Modifier.size(size),
                tint = Color.Black
            )
        }
    }
}