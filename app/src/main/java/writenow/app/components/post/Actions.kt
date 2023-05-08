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
import writenow.app.utils.LaunchedEffectOnce
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
    val (isLiked, setIsLiked) = remember { mutableStateOf(PostState.allPosts.find { it.id == postId }?.isLiked == true) }
    val (currentPost, setCurrentPost) = remember { mutableStateOf(PostState.allPosts.find { it.id == postId }) }
    val (commentsSize, setCommentsSize) = remember { mutableStateOf(PostState.allPosts.find { it.id == postId }?.comments?.size) }
    val (likesSize, setLikesSize) = remember { mutableStateOf(0) }

    LaunchedEffectOnce {
        setLikesSize(Posts.getLikesPerPost(postId))
        setCommentsSize(currentPost?.comments?.size)
    }

    LaunchedEffect(UserState.isCommentClicked) {
        if (UserState.isCommentClicked) setCurrentPost(PostState.allPosts.find { it.id == postId })
    }

    fun updateLike() {
        CoroutineScope(Dispatchers.IO).launch {
            val updatedPost = Posts.getFeed(UserState.id).find { post -> post.id == postId }

            setIsLiked(updatedPost?.isLiked ?: false)
        }
    }

    fun toggleLike() {
        if (currentPost != null) {
            Posts.toggleLike(currentPost.id, UserState.id) {
                if (it && !isLiked) {
                    currentPost.likes = listOf(
                        PostLikes(
                            postId = postId, userId = UserState.id, isUnliked = 1
                        )
                    ) + currentPost.likes
//                    likesSize = likesSize?.plus(1)

                    updateLike()
                }

                if (it && isLiked) {
                    val removed = currentPost.likes.toMutableList()
                        .remove(currentPost.likes.find { post -> post.postId == postId && post.userId == UserState.id })

                    if (removed) {
//                        likesSize = likesSize?.minus(1)
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
                label = commentsSize?.toString() ?: "0",
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UserState.isCommentClicked = true
                UserState.selectedPost = currentPost
                Log.d("PostActions", "${postId}")

                coroutineScope.launch {
                    state.show()

                    UserState.isEllipsisClicked = false
                    UserState.isPostClicked = false
                }
            }
            if (hasEllipsis) {
                More(text = "") {
                    UserState.selectedPost = currentPost

                    openPostMenu(currentPost, coroutineScope, state)
                }
            }
        }
    }
}