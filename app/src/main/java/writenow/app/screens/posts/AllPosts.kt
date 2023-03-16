package writenow.app.screens.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.Layout
import writenow.app.components.bottom.overlay.BottomOverlay
import writenow.app.components.post.Post
import writenow.app.components.post.PostProtector
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.state.PostState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val posts = remember { mutableStateListOf<Post>() }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)


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
                    sheetContent = {},
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
                    })
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState) {
//                itemsIndexed(posts) { _, item ->
//                    Post(
//                        post = item,
//                        navController = navController,
//                        state = state,
//                        coroutineScope = scope
//                    )
//                }
//            }
            } else {
                PostProtector(navController = navController)
            }
        }
    }
}