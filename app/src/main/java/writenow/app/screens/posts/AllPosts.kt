package writenow.app.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import writenow.app.components.Layout
import writenow.app.components.post.LoadingPostContent
import writenow.app.components.post.Post
import writenow.app.components.post.PostContainer
import writenow.app.components.post.PostProtector
import writenow.app.dbtables.Posts
import writenow.app.dbtables.Users
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth
import writenow.app.utils.LaunchedEffectOnce

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val refreshScope = rememberCoroutineScope()
    val context = LocalContext.current

    var refreshing by remember { mutableStateOf(false) }

    LaunchedEffectOnce {
        Users.updateRelationList(UserState.followers)
        Users.updateRelationList(UserState.following, false)
        Users.saveInfo(context, UserState)
    }

    fun refresh() = refreshScope.launch {
        refreshing = true
        PostState.allPosts = Posts.getToDisplay()
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

    Layout(
        title = "WriteNow", navController = navController, lazyListState = lazyListState
    ) { state, scope ->
        if (PostState.isLoading) {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                repeat(5) {
                    PostContainer(height = DefaultWidth / 2) {
                        LoadingPostContent()
                    }
                }
            }
        } else {
            if (UserState.hasPosted) {
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
                            itemsIndexed(PostState.allPosts) { _, item ->
                                Post(
                                    post = item,
                                    navController = navController,
                                    state = state,
                                    coroutineScope = scope
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                        }
                    }
                }
            } else {
                PostProtector(navController = navController)
            }
        }

    }
}