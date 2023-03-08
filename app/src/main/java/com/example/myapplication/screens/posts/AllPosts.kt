package com.example.myapplication.screens.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.Layout
import com.example.myapplication.components.post.Post
import com.example.myapplication.state.PostState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllPosts(navController: NavController, lazyListState: LazyListState) {
    Layout(
        title = "WriteNow", navController = navController, lazyListState = lazyListState
    ) { state, scope ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(25.dp), state = lazyListState
        ) {
            itemsIndexed(PostState.posts) { _, item ->
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