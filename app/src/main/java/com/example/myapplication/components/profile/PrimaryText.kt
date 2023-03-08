package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun PrimaryText(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        modifier = Modifier.padding(8.dp)
    )
}