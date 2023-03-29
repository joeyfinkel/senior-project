package writenow.app.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrange

@Composable
internal fun BaseCircle(
    modifier: Modifier = Modifier,
    border: Modifier? = null,
    size: Dp,
    icon: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        IconButton(
            onClick = onClick, modifier = modifier, colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            Box(
                modifier = Modifier
                    .background(PersianOrange, shape = CircleShape)
                    .then(border ?: Modifier)
                    .size(size),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    } else {
        icon()
    }
}