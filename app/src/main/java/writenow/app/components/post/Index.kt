package writenow.app.components.post

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    fun openMenu() {
        coroutineScope.launch {
            UserState.selectedPost = post
            UserState.isPostClicked = true
            UserState.isCommentClicked = false
            UserState.isEllipsisClicked = false
            state.show()
        }
    }

    Log.d("Post", "Question: ${post?.question}")

    if (post != null) {
        PostContainer(height = DefaultWidth / 2) {
            PostContent(userId = post.uuid,
                username = post.username,
                question = post.question ?: "",
                isEdited = post.isEdited,
                text = post.text,
                datePosted = getPostedDate(post.createdAt),
                navController = navController,
                onLongPress = { openPostMenu(post, coroutineScope, state) }) { openMenu() }
            PostActions(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                state = state,
                coroutineScope = coroutineScope,
                postId = post.id
            )
        }
    }
}
