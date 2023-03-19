package writenow.app.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import writenow.app.R

@Composable
private fun Comment(onClick: (() -> Unit)? = null) {
    val svg = painterResource(id = R.drawable.outline_mode_comment_24)
    val surfaceColor = MaterialTheme.colorScheme.onSurface
    val modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Box(
        modifier = Modifier
            .size(24.dp)
            .then(modifier)
    ) {
        if (onClick == null) {
            Icon(
                painter = svg, contentDescription = "Comment", tint = surfaceColor
            )
        } else {
            IconButton(onClick = onClick) {
                Icon(
                    painter = svg, contentDescription = "Comment", tint = surfaceColor
                )
            }
        }
    }
}

@Composable
fun Comment(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    label: String,
    onClick: () -> Unit
) = IconWithLabel(
    modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
    icon = { Comment() },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) },
    onClick = onClick
)

@Composable
fun Comment(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    label: String,
    onClick: () -> Unit
) = IconWithLabel(modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
    icon = { Comment(onClick = onClick) },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) })

