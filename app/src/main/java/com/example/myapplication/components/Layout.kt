package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.bottomoverlay.comments.Comments
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.AppBar
import com.example.myapplication.ui.theme.DefaultRadius
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Layout(
    title: String,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    val items = listOf("Songs", "Artists", "Playlists")
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    var maxHeight = 1.0

    if (UserState.isEllipsisClicked) maxHeight = 0.35
    else if (UserState.isCommentClicked) maxHeight = 0.5

    Scaffold(
        topBar = {
            Surface(
                color = AppBar,
                shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
            ) {
                SmallTopAppBar(
                    title = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        AccountCircle(50.dp) { println("Clicked") }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = AppBar
                    )
                )
            }
        },
        content = { innerPadding ->
            BottomOverlay(
                sheetContent = {
                    if (UserState.isCommentClicked) {
                        Comments()
                    } else {
                        LazyColumn {
                            items(50) {
                                ListItem(
                                    text = { androidx.compose.material.Text("Item $it") },
                                    icon = {
                                        androidx.compose.material.Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = "Localized description"
                                        )
                                    })
                            }
                        }
                    }
                },
                maxHeight = maxHeight,
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            content(sheetState, scope)
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(innerPadding)
//                    .background(Color.White),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    modifier = Modifier.padding(top = 10.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    content()
//                    Spacer(modifier = Modifier.weight(1f))
//                }
//            }
        },
        bottomBar = {
            if (!sheetState.isVisible)
                Surface(
                    color = AppBar,
                    shape = RoundedCornerShape(topEnd = DefaultRadius, topStart = DefaultRadius)
                ) {
                    BottomAppBar(containerColor = AppBar) {
                        items.forEachIndexed { _, item ->
                            NavigationBarItem(icon = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = item
                                )
                            },
                                label = { Text(item) },
                                selected = false,
                                onClick = { println("Hello") })
                        }
                    }
                }
        })
}