package writenow.app.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.TimePickerDefaults
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import java.time.LocalTime

@Composable
fun ActiveHoursDialog(
    dialogState: MaterialDialogState,
    title: String,
    positiveText: String,
    neutralText: String,
    negativeText: String? = null,
    neutralOnClick: (() -> Unit),
    positiveOnClick: (() -> Unit)? = null,
    negativeOnClick: (() -> Unit)? = null,
    onTimeChange: (LocalTime) -> Unit
) {
    BasicDialog(
        dialogState = dialogState,
        positiveText = positiveText,
        neutralText = neutralText,
        negativeText = negativeText ?: "cancel",
        positiveOnClick = positiveOnClick,
        negativeOnClick = negativeOnClick,
        neutralOnClick = neutralOnClick,
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = title,
            colors = TimePickerDefaults.colors(
                activeBackgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                activeTextColor = MaterialTheme.colorScheme.onSurface,
                borderColor = MaterialTheme.colorScheme.primary,
                selectorColor = MaterialTheme.colorScheme.primary,
                selectorTextColor = MaterialTheme.colorScheme.onSurface,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                inactiveTextColor = MaterialTheme.colorScheme.onSurface,
            ),
            onTimeChange = onTimeChange
        )
    }
}