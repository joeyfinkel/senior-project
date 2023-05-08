package writenow.app.components.bottom.overlay.comments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONObject
import writenow.app.components.TextInput
import writenow.app.components.icons.AccountCircle
import writenow.app.components.icons.Send
import writenow.app.dbtables.Comment
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.PostState
import writenow.app.state.UserState


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Comments(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var commented by remember { mutableStateOf(false) }

    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (current, setCurrent) = remember { mutableStateOf(UserState.selectedPost) }
    val totalComments = derivedStateOf { current?.comments?.size ?: 0 }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        setIsLoading(true)
        setCurrent(UserState.selectedPost)
        setIsLoading(false)
    }

    LaunchedEffect(commented) {
        if (commented) {
            setCurrent(UserState.selectedPost)
            Posts.update(PostState.allPosts)
        }

        commented = false
    }

    fun postComment() {
        val json = JSONObject().apply {
            put("postID", UserState.selectedPost?.id)
            put("userID", UserState.id)
            put("commentText", text)
        }

        Posts.comment(json) {
            if (it) {
                commented = true
                current?.comments = listOf(
                    Comment(
                        userId = UserState.id,
                        postId = UserState.selectedPost?.id!!,
                        text = text,
                        username = UserState.username
                    )
                ) + current?.comments!!
                keyboardController?.hide()

                text = ""
            }
        }

    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "${totalComments.value} comments",
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
            if (totalComments.value == 0) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "There are no comments yet. Be the first to comment!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            } else {
                if (isLoading) {
                    Text(text = "Loading...")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        state = LazyListState(0)
                    ) {
                        current?.let {
                            itemsIndexed(it.comments.sortedByDescending { comment -> comment.dateCommented }) { _, comment ->
                                Comment(comment = comment, navController = navController)
                            }
                        }
                    }
                }
            }
        }

        // The text box that allows users to comment
        TextInput(
            value = text,
            label = "Comment",
            onValueChange = { text = it },
            leadingIcon = {
                AccountCircle(size = 35.dp) {
                    navController.navigate(Screens.UserProfile)
                }
            },
            trailingIcon = {
                Send(enabled = text.isNotEmpty(), onClick = { postComment() })
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { postComment() }),
        )
    }
}