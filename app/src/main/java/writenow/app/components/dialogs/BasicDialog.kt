package writenow.app.components.dialogs

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogScope
import com.vanpra.composematerialdialogs.MaterialDialogState

@Composable
fun BasicDialog(
    dialogState: MaterialDialogState,
    positiveText: String,
    negativeText: String,
    neutralText: String? = null,
    positiveOnClick: (() -> Unit)? = null,
    negativeOnClick: (() -> Unit)? = null,
    neutralOnClick: (() -> Unit)? = null,
    content: @Composable() (MaterialDialogScope.() -> Unit)
) {
    val scope = remember { mutableStateOf<MaterialDialogScope?>(null) }

    MaterialDialog(
        dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(positiveText, onClick = { positiveOnClick?.invoke() })
            negativeButton(negativeText, onClick = { negativeOnClick?.invoke() })
            button(neutralText!!, onClick = { neutralOnClick?.invoke() })
        },
    ) {
        scope.value = this

        content()
    }
}