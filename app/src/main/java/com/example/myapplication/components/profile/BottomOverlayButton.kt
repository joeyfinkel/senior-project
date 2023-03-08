package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.ClickableRow

@Composable
internal fun BottomOverlayButton(
    icon: Painter,
    text: String,
    color: Color = Color.Black,
    onClick: () -> Unit
) = ClickableRow(
    key = text,
    leadingIcon = icon,
    onClick = onClick,
    color = color,
    chevron = false
)

@Composable
internal fun BottomOverlayButton(
    icon: ImageVector,
    text: String,
    color: Color = Color.Unspecified,
    onClick: () -> Unit
) = ClickableRow(
    key = text,
    leadingIcon = icon,
    onClick = onClick,
    color = color,
    chevron = false
)