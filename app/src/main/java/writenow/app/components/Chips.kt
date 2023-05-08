package writenow.app.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Chips(
    values: List<String>,
    activeBackgroundColor: Color = PersianOrange,
    inactiveBackgroundColor: Color = Color.Unspecified,
    disabledBackgroundColor: Color = PersianOrange,
    textColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
    disabledTextColor: Color = Color.White,
    enabled: Boolean? = null,
    onClick: (idx: Int) -> Unit
) {
    val (selectedChip, setSelectedChip) = remember { mutableStateOf(0) }
    val darkMode = isSystemInDarkTheme()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        values.forEachIndexed { index, value ->
            Chip(
                onClick = {
                    setSelectedChip(index)
                    onClick(index)
                },
                enabled = enabled ?: (selectedChip != index),
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (selectedChip == index) {
                        activeBackgroundColor
                    } else {
                        inactiveBackgroundColor
                    },
                    disabledBackgroundColor = disabledBackgroundColor,
                    contentColor = textColor,
                    disabledContentColor = disabledTextColor
                )
            ) {
                Text(text = value, color = placeholderColor(darkMode))
            }
        }
    }
}