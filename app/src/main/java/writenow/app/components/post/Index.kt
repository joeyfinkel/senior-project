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
import writenow.app.dbtables.Post
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth
import writenow.app.utils.getPostedDate
import writenow.app.utils.openPostMenu

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Post(
    post: Post?,
    navController: NavController,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
) {
    var likedPost by remember { mutableStateOf<LikedPost?>(null) }

    var dateText by remember { mutableStateOf("") }

    LaunchedEffect(post?.createdAt) {
        dateText = post?.createdAt?.let { getPostedDate(it) }.toString()
    }

//    LaunchedEffect(Unit) {
//        if (post != null)
//            likedPost = Likes.getLikedPost(UserState.id, post.id)
//
//        // If the post is liked, add it to the liked posts list
//        if (likedPost != null && likedPost?.liked == 1) {
//            PostState.likedPosts.add(likedPost!!)
//            PostState.allPosts.filter { it.id == likedPost?.postId }
//                .forEach { post ->
//                    // add the post to the liked posts list if it's not already there
//                    if (!UserState.likedPosts.contains(post))
//                        UserState.likedPosts.add(post)
//                }
//        }
//    }

    fun openMenu() {
        UserState.selectedPost = post
        UserState.isPostClicked = true
        UserState.isCommentClicked = false
        UserState.isEllipsisClicked = false

        coroutineScope.launch { state.show() }
    }

    if (post != null) {
        PostContainer(height = DefaultWidth / 2) {
            PostContent(userId = post.uuid,
                username = post.username,
                text = post.text,
                datePosted = dateText,
                navController = navController,
                onLongPress = { openPostMenu(post, coroutineScope, state) },
                onClick = { openMenu() })
            PostActions(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state = state,
                coroutineScope = coroutineScope,
                postId = post.id
            )
        }
    }
}
