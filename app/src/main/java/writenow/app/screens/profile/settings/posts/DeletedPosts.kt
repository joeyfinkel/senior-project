package writenow.app.screens.profile.settings.posts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.post.Post
import writenow.app.components.post.PostList
import writenow.app.components.profile.settings.SettingsLayout
import writenow.app.dbtables.Posts
import writenow.app.state.PostState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DeletedPosts(navController: NavController, lazyListState: LazyListState) {
    SettingsLayout(
        title = "Deleted Posts",
        navController = navController,
        content = { innerPadding, state, scope ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(top = 10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PostList(lazyListState = lazyListState, postCallback = {
                            PostState.deletedPosts = Posts.getDeleted(UserState.id).toMutableList()
                            Log.d("DeletedPosts", "DeletedPosts: ${PostState.deletedPosts.size}")
                        }) {
                            itemsIndexed(PostState.deletedPosts) { _, item ->
                                Post(
                                    post = item,
                                    navController = navController,
                                    state = state,
                                    coroutineScope = scope
                                )
                            }
                        }
                    }
                }
            }
        })
}