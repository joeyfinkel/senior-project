package com.example.myapplication.components.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.AccountCircle

/**
 * The content of the post. It will render out the who posted it and what they posted.
 * @param username The username of the user who posted.
 * @param text The contents of the post.
 */
@Composable
fun PostContent(username: String, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AccountCircle(
            35.dp,
            modifier = Modifier.align(Alignment.Top)
        ) {
            // TODO Need to implement going to account page on click
            println("Hello there")
        }
        Column(
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = username,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )
        }

    }
}