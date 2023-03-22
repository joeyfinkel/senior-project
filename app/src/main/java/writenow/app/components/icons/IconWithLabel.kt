package writenow.app.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * The default icon with label component.
 * @param icon The icon to be displayed.
 * @param label The label to be displayed.
 */
@Composable
private fun IconWithLabel(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) {
    icon()
    label()
}

/**
 * Icon with label in a column.
 * @param modifier The modifier to be applied to the icon and label.
 * @param horizontalAlignment The horizontal alignment of the icon and label.
 * @param verticalArrangement The vertical arrangement of the icon and label.
 * @param icon The icon to be displayed.
 * @param label The label to be displayed.
 * @see IconWithLabel
 */
@Composable
fun IconWithLabel(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit
) = Column(
    modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement
) {
    IconWithLabel(icon = icon, label = label)
}

/**
 * Icon with label in a row.
 * @param modifier The modifier to be applied to the icon and label.
 * @param horizontalArrangement The horizontal arrangement of the icon and label.
 * @param verticalAlignment The vertical alignment of the icon and label.
 * @param icon The icon to be displayed.
 * @param label The label to be displayed.
 * @see IconWithLabel
 */
@Composable
fun IconWithLabel(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalAlignment: Alignment.Vertical,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    onClick: () -> Unit
) = Row(
    modifier = modifier.clickable(onClick = onClick),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment
) {
    IconWithLabel(icon = icon, label = label)
}