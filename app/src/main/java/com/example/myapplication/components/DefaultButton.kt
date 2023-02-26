package com.example.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.Primary

@Composable
fun DefaultButton(
    modifier: Modifier = Modifier,
    width: Dp = 110.dp,
    spacedBy: Dp = 15.dp,
    borderRadius: Dp = 20.dp,
    btnText: String,
    icon: ImageVector? = null,
    onBtnClick: () -> Unit
) {
    Button(
        modifier = Modifier
            .widthIn(min = width)
            .then(modifier),
        onClick = onBtnClick,
        colors = ButtonDefaults.buttonColors(containerColor = Primary),
        shape = RoundedCornerShape(borderRadius)
    ) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacedBy),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = icon, contentDescription = "")
                Text(btnText)
            }
        } else {
            Text(btnText)
        }
    }
}