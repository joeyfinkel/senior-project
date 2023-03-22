package writenow.app.components.post

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
fun Chips(values: List<String>, onClick: (Int) -> Unit) {
    var selectedChip by remember { mutableStateOf(0) }

    val darkMode = isSystemInDarkTheme()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        values.forEachIndexed { index, value ->
            Chip(
                onClick = {
                    selectedChip = index
                    onClick(index)
                },
                enabled = selectedChip != index,
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (selectedChip == index) {
                        PersianOrange
                    } else {
                        Color.Unspecified
                    },
                    disabledBackgroundColor = PersianOrange,
                    contentColor = MaterialTheme.colors.onPrimary,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = value, color = placeholderColor(darkMode))
            }
        }
    }
}