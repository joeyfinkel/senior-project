package com.example.myapplication.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun Like(onClick: (color: Color) -> Unit) {
    var color by remember { mutableStateOf(Color.Black) }
    var icon by remember { mutableStateOf(Icons.Default.FavoriteBorder) }

    fun toggleLike() {
        val filled = Icons.Default.Favorite
        val border = Icons.Default.FavoriteBorder

        color = if (color == Color.Red) Color.Black else Color.Red
        icon = if (icon == filled) border else filled
    }

    IconButton(onClick = {
        toggleLike()
        onClick(color)
    }) {
        Icon(
            imageVector = icon,
            contentDescription = "Like",
            tint = color
        )
    }
}