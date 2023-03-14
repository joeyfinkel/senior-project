package writenow.app.components.icons

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun Like(isLiked: Boolean = false, onClick: (color: Color) -> Unit) {
    val filled = Icons.Default.Favorite
    val border = Icons.Default.FavoriteBorder
    val isDarkMode = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.onSurface

    var color by remember { mutableStateOf(if (isLiked) Color.Red else surfaceColor) }
    var icon by remember { mutableStateOf(if (isLiked) filled else border) }

    fun toggleLike() {
        color = if (color == Color.Red && isDarkMode) surfaceColor else Color.Red
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