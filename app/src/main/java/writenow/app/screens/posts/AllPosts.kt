package writenow.app.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.Layout
import writenow.app.components.post.*
import writenow.app.dbtables.Posts
import writenow.app.state.GlobalState
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth
import writenow.app.utils.LaunchedEffectOnce

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val hasPosted = remember { derivedStateOf { UserState.hasPosted } }

    LaunchedEffectOnce {
        if (GlobalState.user == null) {
            GlobalState.user = GlobalState.userRepository.getUser()

            if (GlobalState.user != null) GlobalState.userRepository.addUser(GlobalState.user!!)
        }
    }

    Layout(
        title = "WriteNow", navController = navController, lazyListState = lazyListState
    ) { state, scope ->
        if (PostState.isLoading && hasPosted.value) {
            LoadingPosts(
                postContainerHeight = DefaultWidth / 2,
                renderTotal = 5,
                verticalArrangement = Arrangement.spacedBy(25.dp)
            )
        } else {
            if (hasPosted.value) {
                PostList(
                    lazyListState = lazyListState,
                    postCallback = { PostState.allPosts = Posts.getFeed(UserState.id) },
                ) {
                    itemsIndexed(PostState.allPosts) { _, item ->
                        Post(
                            post = item,
                            navController = navController,
                            state = state,
                            coroutineScope = scope
                        )
                    }
                }
                if (PostState.allPosts.isEmpty()) {
                    Text(
                        text = "None of your friends have made any posts yet!\nCheck the discover tab to find new friends.",
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                PostProtector(navController = navController)
            }
        }

    }
}