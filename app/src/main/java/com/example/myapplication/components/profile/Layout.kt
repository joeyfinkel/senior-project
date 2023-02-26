package com.example.myapplication.components.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.More

@Composable
fun ProfileLayout(
    title: String,
    hasEllipsis: Boolean = false,
    onBackClick: () -> Unit,
    additionalTopContent: @Composable (ColumnScope.() -> Unit)? = null,
    bottomContent: @Composable (ColumnScope.() -> Unit)
) {
    Column(modifier = Modifier.fillMaxHeight()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                Text(text = title)
                if (hasEllipsis) More {/*TODO show account options*/ } else Box(
                    modifier = Modifier.width(
                        40.dp
                    ), content = {})
            }
            additionalTopContent?.invoke(this)
        }
        bottomContent()
    }
}