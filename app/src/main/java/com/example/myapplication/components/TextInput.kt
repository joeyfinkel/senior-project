package com.example.myapplication.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBar

/**
 * The default text field for the app.
 * @param value The value of the input.
 */
@Composable
fun TextInput(
    value: String,
    label: String,
    errorText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    spacer: Boolean = true,
    width: Dp = 300.dp
) {
    val myModifier = if (width != 300.dp) modifier.then(modifier.width(width)) else modifier
    val colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = AppBar,
        unfocusedBorderColor = AppBar,

        )


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = myModifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        isError = isError,
        colors = colors
    )

    if (errorText.isNotEmpty() && isError) Text(errorText)
    if (spacer) Spacer(modifier = Modifier.height(20.dp))
}



