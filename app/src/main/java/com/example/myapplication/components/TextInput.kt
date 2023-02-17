package com.example.myapplication.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The default text field for the app.
 * @param value The value of the input.
 */
@Composable
fun TextInput(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    spacer: Boolean = true,
    errorText: String,
    width: Dp = 300.dp
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier.width(width),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        isError = isError
    )
    if (errorText.isNotEmpty() && isError) Text(errorText)
    if (spacer) Spacer(modifier = Modifier.height(20.dp))
}



