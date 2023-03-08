package com.example.myapplication.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.DefaultWidth
import com.example.myapplication.ui.theme.PostBG
import com.example.myapplication.ui.theme.Primary

@Composable
fun PostContainer(
    modifier: Modifier = Modifier,
    fullHeight: Boolean = false,
    width: Dp = DefaultWidth + 100.dp,
    height: Dp? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .width(width = width)
            .then(
                if (fullHeight) {
                    Modifier.fillMaxSize()
                } else {
                    Modifier.height(height = height ?: 200.dp)
                }
            )
            .then(modifier)
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .border(2.dp, Primary, RoundedCornerShape(16.dp))
                .background(PostBG)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                content()
            }
        }
    }
}