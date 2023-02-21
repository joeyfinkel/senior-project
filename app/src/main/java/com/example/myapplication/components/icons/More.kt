package com.example.myapplication.components.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.myapplication.R

@Composable
fun More(onClick: () -> Unit) {
    val svg = painterResource(id = R.drawable.outline_more_horiz_24)

    IconButton(onClick = onClick) {
        Icon(painter = svg, contentDescription = "Comment")
    }
}