package writenow.app.screens.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.Layout
import writenow.app.components.post.Post
import writenow.app.components.post.PostProtector
import writenow.app.dbtables.Post
import writenow.app.dbtables.Posts
import writenow.app.state.PostState
import writenow.app.state.UserState
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    val posts = remember { mutableStateListOf<Post>() }

    LaunchedEffect(Unit) {
        val date = LocalDate.now().dayOfMonth
        posts.addAll(Posts.getAll())

        PostState.posts = posts
        UserState.posts =
            posts.filter { post -> post.username == UserState.username }.toMutableList()

    }

    Layout(
        title = "WriteNow",
        navController = navController,
        lazyListState = lazyListState
    ) { state, scope ->
        if (UserState.hasPosted) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState) {
                itemsIndexed(posts) { _, item ->
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