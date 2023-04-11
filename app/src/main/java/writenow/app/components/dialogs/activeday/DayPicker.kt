package writenow.app.components.dialogs.activeday

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DayPicker(
    daysOfWeek: List<String> = listOf("su", "m", "t", "w", "th", "f", "s"),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    shape: Shape = RoundedCornerShape(50.dp),
    buttonSize: Dp = 40.dp,
    onSelect: (String) -> Unit
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = Modifier.fillMaxWidth()
    ) {
        daysOfWeek.forEach { day ->
            Button(
                onClick = { onSelect(day) },
                shape = shape,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(buttonSize)
            ) {
                Text(text = day, color = textColor)
            }
        }
    }
}