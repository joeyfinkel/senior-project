package com.example.myapplication.components.icons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Primary

@Composable
internal fun BaseCircle(
    modifier: Modifier = Modifier,
    border: Modifier? = null,
    size: Dp = 50.dp,
    icon: @Composable () -> Unit,
    onClick: (() -> Unit)? = null
) {
    if (onClick != null) {
        IconButton(onClick = onClick, modifier = modifier) {
            Box(
                modifier = Modifier
                    .background(Primary, shape = CircleShape)
                    .then(border ?: Modifier)
                    .size(size),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
        }
    } else {
        icon()
    }
}