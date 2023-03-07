package com.example.myapplication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
private fun ClickableRow(
    key: String,
    value: String? = null,
    trailingText: Boolean = true,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = Color.Unspecified,
    leadingIcon: @Composable (() -> Unit)? = null,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(height)
    ) {
        if (leadingIcon != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                leadingIcon()
                Text(text = key, color = color)
            }
        } else {
            Text(text = key, color = color)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (trailingText && value != null) Text(text = value)
            if (chevron) Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Right",
                modifier = Modifier.size(25.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    trailingText: Boolean = true,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = Color.Black,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    trailingText = trailingText,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = null,
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    trailingText: Boolean = true,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = Color.Black,
    leadingIcon: ImageVector,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    trailingText = trailingText,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            imageVector = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    onClick = onClick
)

@Composable
fun ClickableRow(
    key: String,
    value: String? = null,
    trailingText: Boolean = true,
    chevron: Boolean = true,
    height: Dp = 50.dp,
    color: Color = Color.Black,
    leadingIcon: Painter,
    onClick: () -> Unit
) = ClickableRow(
    key = key,
    value = value,
    trailingText = trailingText,
    chevron = chevron,
    height = height,
    color = color,
    leadingIcon = {
        Icon(
            painter = leadingIcon,
            contentDescription = key,
            modifier = Modifier.size(25.dp),
            tint = color
        )
    },
    onClick = onClick
)