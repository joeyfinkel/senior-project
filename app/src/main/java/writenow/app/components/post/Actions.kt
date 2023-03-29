package writenow.app.components.post

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import writenow.app.components.icons.Comment
import writenow.app.components.icons.Like
import writenow.app.components.icons.More
import writenow.app.dbtables.PostLikes
import writenow.app.dbtables.Posts
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.utils.openPostMenu

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostActions(
    modifier: Modifier,
    state: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    postId: Int,
    hasEllipsis: Boolean = true
) {
    var isLiked by remember { mutableStateOf(PostState.allPosts.find { it.id == postId }?.isLiked == true) }
    var currentPost by remember { mutableStateOf(PostState.allPosts.find { it.id == postId }) }
    var commentsSize by remember { mutableStateOf(PostState.allPosts.find { it.id == postId }?.comments?.size) }
    var likesSize by remember { mutableStateOf(PostState.allPosts.find { it.id == postId }?.likes?.filter { it.isUnliked == 0 }?.size) }

    LaunchedEffect(commentsSize) {
        if (commentsSize == null) {
            Log.d("PostActions", "size is null")
            return@LaunchedEffect
        }

        currentPost = PostState.allPosts.filter { it.id == postId }[0]
        commentsSize = currentPost?.comments?.size
    }

    fun updateLike() {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedPost = Posts.getToDisplay().find { post -> post.id == postId }

            isLiked = updatedPost?.isLiked ?: false
        }
    }

    fun toggleLike() {
        if (currentPost != null) {
            Posts.toggleLike(currentPost!!.id, UserState.id) {
                if (it && !isLiked) {
                    currentPost?.likes = listOf(
                        PostLikes(
                            postId = postId, userId = UserState.id, isUnliked = 1
                        )
                    ) + (currentPost?.likes ?: emptyList())
                    likesSize = likesSize?.plus(1)

                    updateLike()
                }

                if (it && isLiked) {
                    val removed = currentPost?.likes?.toMutableList()
                        ?.remove(currentPost?.likes?.find { post -> post.postId == postId && post.userId == UserState.id })

                    if (removed == true) {
                        likesSize = likesSize?.minus(1)
                    }

                    updateLike()
                }

            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier), contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Like(isLiked = isLiked,
                label = likesSize.toString(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                toggleLike = { toggleLike() })
            Comment(
                label = commentsSize.toString(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UserState.isCommentClicked = true
                UserState.isEllipsisClicked = false
                UserState.isPostClicked = false
                UserState.selectedPost = currentPost

                if (UserState.isCommentClicked) coroutineScope.launch { state.show() }
            }
            if (hasEllipsis) {
                More(text = "") { openPostMenu(currentPost, coroutineScope, state) }
            }
        }
    }
}