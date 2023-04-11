package writenow.app.components.dialogs.activeday

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.customView
import writenow.app.components.dialogs.BasicDialog

@Composable
fun ActiveDayDialog(
    dialogState: MaterialDialogState,
    onDaySelected: (String) -> Unit,
    additionalText: @Composable () -> Unit,
    positiveOnClick: (() -> Unit),
    neutralOnClick: (() -> Unit)
) {
    BasicDialog(
        dialogState = dialogState,
        positiveText = "Lock in",
        negativeText = "Cancel",
        neutralText = "Reset",
        positiveOnClick = positiveOnClick,
        neutralOnClick = neutralOnClick
    ) {
        customView {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.align(Alignment.Start)) {
                    Text(text = "Select active days", color = MaterialTheme.colorScheme.onSurface)
                }
                DayPicker(onSelect = onDaySelected, buttonSize = 35.dp)
                Box(modifier = Modifier.align(Alignment.Start)) {
                    additionalText()
                }
            }
        }
    }
}