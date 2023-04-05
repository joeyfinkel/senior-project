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
import writenow.app.dbtables.Users
import writenow.app.state.GlobalState
import writenow.app.state.PostState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultWidth
import writenow.app.utils.LaunchedEffectOnce

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    LaunchedEffectOnce {
        Users.updateRelationList(UserState.followers)
        Users.updateRelationList(UserState.following, false)

        if (GlobalState.user == null) {
            GlobalState.user = GlobalState.userRepository.getUser()

            GlobalState.userRepository.addUser(GlobalState.user!!)
        }
    }

    Layout(
        title = "WriteNow", navController = navController, lazyListState = lazyListState
    ) { state, scope ->
        if (PostState.isLoading && UserState.hasPosted) {
            Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                repeat(5) {
                    PostContainer(height = DefaultWidth / 2) {
                        LoadingPostContent()
                    }
                }
            }
        } else {
            if (UserState.hasPosted) {
                PostList(
                    lazyListState = lazyListState,
                    postCallback = { PostState.allPosts = Posts.getToDisplay() },
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
            } else {
                PostProtector(navController = navController)
            }
        }

    }
}