package com.example.myapplication.components.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.icons.Comment
import com.example.myapplication.components.icons.Like
import com.example.myapplication.components.icons.More
import com.example.myapplication.dbtables.Post
import com.example.myapplication.state.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostActions(
    modifier: Modifier,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    post: Post,
    onLike: (Color) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Like(post.isLiked) { onLike(it) }
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