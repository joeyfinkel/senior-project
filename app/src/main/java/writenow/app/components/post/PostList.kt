package writenow.app.components.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Displays a list of posts that can be refreshed.
 * @param lazyListState The state of the lazy list.
 * @param postCallback The callback to get the posts. This is the callback
 * that will be called when the user pulls to refresh.
 * @param posts The posts to display. This should be an [itemsIndexed] block with the same variables used in [postCallback].
 * @sample writenow.app.screens.posts.AllPosts
 *
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostList(
    lazyListState: LazyListState,
    postCallback: suspend CoroutineScope.() -> Unit,
    posts: LazyListScope.() -> Unit,
) {
    var refreshing by remember { mutableStateOf(false) }

    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        refreshing = true
        postCallback(this)
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refresh() },
        indicator = { pullState, _ ->
            PullRefreshIndicator(
                refreshing = pullState.isRefreshing,
                state = refreshState,
                contentColor = MaterialTheme.colorScheme.primary,
                backgroundColor = Color.Transparent,
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .pullRefresh(refreshState)
                .padding(if (refreshing) 70.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp),
            state = lazyListState
        ) {
            if (!refreshing) {
                posts()

                item {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}