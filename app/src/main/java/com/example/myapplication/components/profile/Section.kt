package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Primary

@Composable
fun Section(
    title: String? = null,
    columnSpacing: Dp = 15.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (title != null) Text(text = title, color = Primary, fontWeight = FontWeight.Bold)
        Column(verticalArrangement = Arrangement.spacedBy(columnSpacing)) {
            content()
        }
    }
}