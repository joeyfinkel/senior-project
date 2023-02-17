package com.example.myapplication.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedColumn(
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(
            animationSpec = tween(durationMillis = 500, delayMillis = 200)
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(300.dp)
                .height(500.dp),
        ) {
            Column(
                horizontalAlignment = horizontalAlignment,
                verticalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }

}