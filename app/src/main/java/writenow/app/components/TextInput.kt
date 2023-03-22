package writenow.app.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor

/**
 * The default text field for the app.
 * @param value The value of the input.
 */
@Composable
fun TextInput(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    hint: String? = null,
    errorText: String = "",
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    spacer: Boolean = true,
    width: Dp = 280.dp
) {
    val darkMode = isSystemInDarkTheme()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true,
        modifier = if (width != 300.dp) modifier.then(modifier.width(width)) else modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        isError = isError,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            placeholderColor = placeholderColor(darkMode),
            unfocusedLabelColor = placeholderColor(darkMode),
            focusedBorderColor = PersianOrange,
            unfocusedBorderColor = PersianOrange,
            cursorColor = PersianOrange,
        )
    )

    if (hint != null) Text(hint, color = Color.Gray)
    if (errorText.isNotEmpty() && isError) Text(errorText)
    if (spacer) Spacer(modifier = Modifier.height(20.dp))
}



