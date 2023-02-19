package com.example.myapplication.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBar

@Composable
fun DefaultButton(
    width:Dp = 110.dp,
    btnText: String, onBtnClick: () -> Unit
) {
    Button(
        modifier = Modifier.widthIn(min = width),
        onClick = onBtnClick,
        colors = ButtonDefaults.buttonColors(containerColor = AppBar)
    ) {
        Text(btnText)
    }
}