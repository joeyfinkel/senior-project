package com.example.myapplication.components.post

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.myapplication.dbtables.Post
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.DefaultWidth
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Post(
    post: Post,
    navController: NavController,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {
    PostContainer(height = DefaultWidth / 2) {
        PostContent(
            userId = post.userId,
            username = post.username,
            text = post.text,
            navController = navController
        )
        PostActions(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            state = state,
            coroutineScope = coroutineScope,
            post = post,
            onLike = {
                val likedPosts = UserState.likedPosts

                if (it == Color.Red) {
                    post.isLiked = true

                    if (!likedPosts.contains(post)) likedPosts.add(post)
                } else {
                    post.isLiked = false

                    if (likedPosts.contains(post)) likedPosts.remove(post)
                }

            }
        )
    }
}
