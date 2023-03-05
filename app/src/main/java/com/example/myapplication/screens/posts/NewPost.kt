package com.example.myapplication.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.DefaultButton
import com.example.myapplication.components.TopBar
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.post.PostContainer
import com.example.myapplication.dbtables.Post
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.NewPostState
import com.example.myapplication.state.PostState
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.PostBG

@Composable
fun NewPost(navController: NavController) {
    val focusRequester = FocusRequester()
    val posts = PostState.posts

    var value by remember { mutableStateOf("") }
    var keyboardShown by remember { mutableStateOf(false) }

    LaunchedEffect(keyboardShown) {
        if (keyboardShown) {
            focusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            leadingIcon = {
                ClickableText(
                    text = AnnotatedString("Cancel"),
                    onClick = { navController.popBackStack() }
                )
            },
            trailingIcon = {
                DefaultButton(
                    btnText = "Post",
                    enabled = value.isNotEmpty() || value.isNotBlank(),
                    onBtnClick = {
                        if (value.isNotEmpty() || value.isNotBlank()) {
                            posts.add(
                                Post(
                                    posts.lastIndex + 1,
                                    UserState.id,
                                    UserState.username,
                                    value,
                                    0,
                                    0,
                                    "",
                                )
                            )
                        }
                        navController.navigate(Screens.Posts.route)
                    }
                )
            }
        )
        PostContainer(fullHeight = true) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AccountCircle(
                    size = 35.dp,
                    modifier = Modifier.align(Alignment.Top)
                )
                Column(
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 5.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = value,
                        placeholder = { Text(text = "New Post") },
                        onValueChange = { value = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 5.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { keyboardShown = it.isFocused },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = PostBG,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }
}