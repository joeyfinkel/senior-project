package writenow.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.R

@Composable
private fun ClickableRow(
    key: String,
    value: String? = null,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onClick: () -> Unit
) = Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .height(height)
) {
    if (leadingIcon != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            leadingIcon()
            Text(text = key, color = color)
        }
    } else {
        Text(text = key, color = color)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        if (value != null) Text(text = value, color = color)
        if (chevron && trailingIcon == null) Icon(
            painter = painterResource(id = R.drawable.chevron_right),
            contentDescription = "Right",
            modifier = Modifier.size(25.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
        if (trailingIcon != null) trailingIcon()
    }
}

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = null,
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: ImageVector,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            imageVector = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    trailingIcon = {
        if (trailingIcon != null)
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = color
            )
    },
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: Painter,
    trailingIcon: ImageVector? = null,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            painter = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: Painter,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    chevron = false,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            painter = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    trailingIcon = trailingIcon,
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    height: Dp = 50.dp,
    color: Color = MaterialTheme.colorScheme.onSurface,
    leadingIcon: ImageVector,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    chevron = false,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            imageVector = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    trailingIcon = trailingIcon,
    onClick = onClick
)