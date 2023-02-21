package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.icons.Comment
import com.example.myapplication.components.icons.Like
import com.example.myapplication.components.icons.More
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.AppBar
import com.example.myapplication.ui.theme.DefaultWidth
import com.example.myapplication.ui.theme.PostBG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Post(
    username: String,
    text: String,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    Box(
        modifier = Modifier
            .width(DefaultWidth + 50.dp)
            .height(DefaultWidth / 2)
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .border(2.dp, AppBar, RoundedCornerShape(16.dp))
                .background(PostBG)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AccountCircle(
                        35.dp,
                        modifier = Modifier.align(Alignment.Top)
                    ) { println("Hello there") }
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
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.spacedBy(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Like {
                            // Change DB table data here...
                            if (it == Color.Red) {
                                println("You liked the post")
                            } else {
                                println("You unliked the post")
                            }
                        }
                        Comment {
                            UserState.isCommentClicked = true
                            UserState.isEllipsisClicked = false

                            if (UserState.isCommentClicked) coroutineScope.launch { state.show() }
                        }
                        More {
                            UserState.isEllipsisClicked = true
                            UserState.isCommentClicked = false

                            if (UserState.isEllipsisClicked) coroutineScope.launch { state.show() }

                        }
                    }
                }
            }
        }
    }
}
