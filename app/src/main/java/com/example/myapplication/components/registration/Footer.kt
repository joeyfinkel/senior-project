package com.example.myapplication.components.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegistrationFooter(
    btnText: String,
    additionalText: String = "",
    onBtnClick: () -> Unit,
    onTextClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (additionalText.isNotEmpty() && onTextClick != null) {
            Text(
                text = additionalText,
                modifier = Modifier
                    .clickable(onClick = onTextClick)
                    .weight(1f)
            )
        }
        Button(modifier = Modifier.widthIn(min = 100.dp), onClick = onBtnClick) {
            Text(btnText)
        }
    }
}