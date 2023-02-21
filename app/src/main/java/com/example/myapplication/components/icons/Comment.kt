package com.example.myapplication.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AppBar

@Composable
fun Comment(onClick: () -> Unit) {
    val svg = painterResource(id = R.drawable.outline_mode_comment_24)

    IconButton(onClick = onClick) {
        Icon(painter = svg, contentDescription = "Comment")
    }
}