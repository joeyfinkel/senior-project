package writenow.app.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.Layout
import writenow.app.components.post.Post
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.dbtables.Users
import writenow.app.state.PostState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val posts = remember { mutableStateListOf<Post>() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Users.saveLoginInfo(context, UserState.username, UserState.password)

        if (posts.isEmpty()) posts.addAll(Posts.getAll())

        PostState.allPosts = posts
    }

    Layout(
        title = "WriteNow",
        navController = navController,
        lazyListState = lazyListState
    ) { state, scope ->
        LazyColumn(verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState) {
            itemsIndexed(posts) { _, item ->
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
//        if (UserState.hasPosted) {
//            LazyColumn(verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState) {
//                itemsIndexed(posts) { _, item ->
//                    Post(
//                        post = item,
//                        navController = navController,
//                        state = state,
//                        coroutineScope = scope
//                    )
//                }
//                item {
//                    Spacer(modifier = Modifier.height(2.dp))
//                }
//            }
//        } else {
//            PostProtector(navController = navController)
//        }
    }
}