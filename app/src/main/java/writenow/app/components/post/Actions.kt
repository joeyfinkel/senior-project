package writenow.app.components.post

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.components.icons.Comment
import writenow.app.components.icons.Like
import writenow.app.components.icons.More
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostActions(
    modifier: Modifier, state: ModalBottomSheetState, coroutineScope: CoroutineScope, postId: Int
) {
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
            Like(postId = postId)
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