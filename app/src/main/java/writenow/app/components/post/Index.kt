package writenow.app.components.post

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.dbtables.LikedPost
import writenow.app.dbtables.LikesAndComments
import writenow.app.dbtables.Post
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Post(
    post: Post?,
    navController: NavController,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {
    var likedPost by remember { mutableStateOf<LikedPost?>(null) }

    LaunchedEffect(Unit) {
        if (post != null)
            likedPost = LikesAndComments.getLikedPost(UserState.id, post.id)

        // If the post is liked, add it to the liked posts list
        if (likedPost != null && likedPost?.liked == 1) {
            PostState.likedPosts.add(likedPost!!)
            PostState.allPosts.filter { it.id == likedPost?.postId }
                .forEach { post ->
                    // add the post to the liked posts list if it's not already there
                    if (!UserState.likedPosts.contains(post))
                        UserState.likedPosts.add(post)
                }
        }
    }

    if (post != null) {
        PostContainer(height = DefaultWidth / 2) {
            PostContent(
                userId = post.uuid,
                username = post.username,
                text = post.text,
                navController = navController,
                onClick = {
                    UserState.selectedPost = post
                    UserState.isPostClicked = true
                    UserState.isCommentClicked = false
                    UserState.isEllipsisClicked = false

                    coroutineScope.launch { state.show() }
                },
            )
            PostActions(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state = state,
                coroutineScope = coroutineScope,
                postId = post.id
            )
        }
    }
}
