package writenow.app.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
private fun Like(isLiked: Boolean, toggleLike: () -> Unit) {
    val filled = Icons.Default.Favorite
    val border = Icons.Default.FavoriteBorder
    val surfaceColor = MaterialTheme.colorScheme.onSurface

    var color by remember { mutableStateOf(if (isLiked) Color.Red else surfaceColor) }
    var icon by remember { mutableStateOf(if (isLiked) filled else border) }

    LaunchedEffect(isLiked) {
        color = if (isLiked) Color.Red else surfaceColor
        icon = if (isLiked) filled else border
    }

    Box(modifier = Modifier
        .size(24.dp)
        .clickable { toggleLike() }) {
        IconButton(
            onClick = { toggleLike() }, modifier = Modifier.padding(bottom = 0.dp)
        ) {
            Icon(imageVector = icon, contentDescription = "Like", tint = color)
        }
    }
}

@Composable
fun Like(
    isLiked: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    toggleLike: () -> Unit,
) = IconWithLabel(modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
    icon = { Like(isLiked = isLiked, toggleLike = toggleLike) },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) })

@Composable
fun Like(
    isLiked: Boolean,
    label: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    toggleLike: () -> Unit,
) = IconWithLabel(modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
    icon = { Like(isLiked = isLiked, toggleLike = toggleLike) },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) },
    onClick = { })

