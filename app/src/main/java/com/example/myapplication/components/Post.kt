package com.example.myapplication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.AppBar
import com.example.myapplication.ui.theme.DefaultWidth
import com.example.myapplication.ui.theme.PostBG

@Composable
fun Post() {
    BoxWithConstraints(
        modifier = Modifier
            .width(DefaultWidth + 50.dp)
            .height(DefaultWidth / 2)
            .border(BorderStroke(2.dp, AppBar), shape = RoundedCornerShape(25.dp))
            .background(PostBG)
    ) {
//        Text(text = "Hello")
    }
}