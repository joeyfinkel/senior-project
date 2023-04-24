package writenow.app.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.LocalDate

@Composable
fun DatePicker(
    dialogState: MaterialDialogState,
    title: String,
    positiveText: String,
    neutralText: String? = null,
    negativeText: String? = null,
    neutralOnClick: (() -> Unit)? = null,
    positiveOnClick: (() -> Unit)? = null,
    negativeOnClick: (() -> Unit)? = null,
    onDateChange: (LocalDate) -> Unit
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
        datepicker(
            initialDate = LocalDate.now(), title = title, colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onSurface,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onSurface,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                dateActiveTextColor = MaterialTheme.colorScheme.onSurface,
                dateInactiveTextColor = MaterialTheme.colorScheme.onSurface,
            ), onDateChange = onDateChange
        )
    }
}