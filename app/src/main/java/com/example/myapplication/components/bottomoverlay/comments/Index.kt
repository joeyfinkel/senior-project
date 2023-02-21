package com.example.myapplication.components.bottomoverlay.comments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.icons.AccountCircle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Comments() {
    var comment by remember { mutableStateOf("") }
    val totalComments = 50

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$totalComments comments",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp),
                state = LazyListState(0)
            ) {
                items(50) { index ->
                    Comment(fromUser = "User ${index + 1}", comment = "Comment number ${index + 1}")
                }
            }
        }
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            icon = { AccountCircle(35.dp) { println("Hello") } }, text = {
                TextInput(
                    value = comment,
                    label = "Comment",
                    errorText = "",
                    onValueChange = { comment = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}