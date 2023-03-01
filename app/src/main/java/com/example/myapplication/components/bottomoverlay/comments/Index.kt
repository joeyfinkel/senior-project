package com.example.myapplication.components.bottomoverlay.comments

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.icons.Send
import com.example.myapplication.screens.Screens


fun commentCreator(total: Int): MutableList<String> {
    val comments = mutableListOf<String>()

    for (i in 0..total) {
        println("Adding comment $i...")
        comments.add("Comment $i")
    }

    return comments
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Comments(navController: NavController) {
    var newComment by remember { mutableStateOf("") }
    val comments by remember { mutableStateOf(commentCreator(2).toList()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    fun postComment() {
        comments.plus(newComment)
        keyboardController?.hide()

        newComment = ""
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "${comments.size} comments",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        // Renders out the list of comments
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
                itemsIndexed(comments) { idx, comment ->
                    Comment(
                        userId = idx,
                        username = "User ${idx + 1}",
                        comment = comment,
                        navController = navController
                    )
                }
            }
        }
        // The text box that allows users to comment
        TextInput(
            value = newComment,
            label = "Comment",
            onValueChange = { newComment = it },
            leadingIcon = {
                AccountCircle(size = 35.dp) {
                    navController.navigate(Screens.UserProfile.route)
                }
            },
            trailingIcon = {
                Send(enabled = newComment.isNotEmpty(), onClick = { postComment() })
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { postComment() }),
        )
    }
}