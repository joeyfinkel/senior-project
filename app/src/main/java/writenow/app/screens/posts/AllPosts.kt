package writenow.app.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import writenow.app.components.Layout
import writenow.app.components.bottom.overlay.BottomOverlay
import writenow.app.components.icons.More
import writenow.app.components.post.Post
import writenow.app.components.post.PostContent
import writenow.app.components.post.PostProtector
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.state.PostState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val posts = remember { mutableStateListOf<Post>() }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (posts.isEmpty()) posts.addAll(Posts.getAll())

        PostState.allPosts = posts
    }

    CompositionLocalProvider(UserState.selectedPostState provides sheetState) {
        Layout(
            title = "WriteNow", navController = navController, lazyListState = lazyListState
        ) { state, scope ->
            if (UserState.hasPosted) {
                BottomOverlay(
                    sheetContent = {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = { scope.launch { sheetState.hide() } }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close post"
                                    )
                                }
                            }
                            if (UserState.selectedPost != null)
                                PostContent(
                                    userId = UserState.selectedPost!!.uuid,
                                    username = UserState.selectedPost!!.username,
                                    navController = navController,
                                    text = UserState.selectedPost!!.text,
                                    trailingIcon = { More { /* TODO */ } },
                                )
                        }
                    },
                    fullScreen = true,
                    sheetState = sheetState,
                    content = {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState
                        ) {
                            itemsIndexed(posts) { _, item ->
                                Post(
                                    post = item,
                                    navController = navController,
                                    state = state,
                                    coroutineScope = scope
                                )
                            }
                        }
                    }
                )
            } else {
                PostProtector(navController = navController)
            }
        }
    }
}