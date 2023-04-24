package writenow.app.components.profile

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import writenow.app.components.ClickableRow

@Composable
internal fun BottomOverlayButton(
    icon: Painter,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) = ClickableRow(
    key = text, chevron = false, color = color, leadingIcon = icon, onClick = onClick
)

@Composable
internal fun BottomOverlayButton(
    icon: ImageVector,
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) = ClickableRow(
    key = text, chevron = false, color = color, leadingIcon = icon, onClick = onClick
)

@Composable
internal fun BottomOverlayButton(
    text: String, color: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit
) = ClickableRow(
    key = text, chevron = false, color = color, onClick = onClick
)