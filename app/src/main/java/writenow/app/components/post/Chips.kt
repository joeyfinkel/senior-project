package writenow.app.components.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.Primary

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Chips(values: List<String>) {
    var selectedChip by remember { mutableStateOf(0) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        values.forEachIndexed { index, value ->
            Chip(
                onClick = { selectedChip = index },
                enabled = selectedChip != index,
                colors = ChipDefaults.chipColors(
                    backgroundColor = if (selectedChip == index) {
                        Primary
                    } else {
                        Color.Unspecified
                    },
                    disabledBackgroundColor = Primary,
                    contentColor = Color.Black,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = value)
            }
        }
    }
}