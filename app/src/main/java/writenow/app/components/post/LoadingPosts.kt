package writenow.app.components.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun LoadingPosts(
    postContainerHeight: Dp, renderTotal: Int
) {
    repeat(renderTotal) {
        PostContainer(height = postContainerHeight) {
            LoadingPostContent()
        }
    }
}

@Composable
fun LoadingPosts(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    postContainerHeight: Dp,
    renderTotal: Int
) {
    Column(
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
    ) {
        LoadingPosts(postContainerHeight = postContainerHeight, renderTotal = renderTotal)
    }
}