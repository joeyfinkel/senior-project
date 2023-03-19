package writenow.app.components.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Send(
    color: Color = MaterialTheme.colorScheme.onSurface,
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, enabled = enabled) {
        Icon(
            Icons.Default.Send,
            contentDescription = "Send",
            tint = color
        )
    }
}