package com.example.myapplication.components.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Colors
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.Primary

@Composable
fun RowData(primaryText: String, secondaryText: String, onClick: (() -> Unit)? = null) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onClick == null) {
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .background(Primary, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                PrimaryText(text = primaryText)
            }
        } else {
            TextButton(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Primary,
                    contentColor = Color.Black
                ),
                modifier = Modifier.width(75.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                PrimaryText(text = primaryText)
            }
        }
        Text(text = secondaryText)
    }
}
