package com.example.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.bottom.overlay.comments.Comments
import com.example.myapplication.components.bottombar.BottomBar
import com.example.myapplication.components.bottomoverlay.BottomOverlay
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.icons.AddCircle
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.SelectedUserState
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.DefaultRadius
import com.example.myapplication.ui.theme.Primary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The general layout of the entire app.
 * This function renders out the top bar, content, and bottom bar.
 * @param title The title of the current screen.
 * @param content The main content of the current page (**placed in between the top and bottom bar**).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun Layout(
    title: String,
    navController: NavController,
    lazyListState: LazyListState? = null,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    var maxHeight = 1.0

    if (UserState.isEllipsisClicked) maxHeight = 0.35
    else if (UserState.isCommentClicked) maxHeight = 0.5

    Scaffold(
        topBar = {
            Surface(
                color = Primary,
                shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
            ) {
                SmallTopAppBar(
                    title = {
                        if (lazyListState != null) {
                            ClickableText(
                                text = AnnotatedString(title),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                                onClick = { scope.launch { lazyListState.scrollToItem(0) } }
                            )
                        } else {
                            Text(
                                title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    },
                    actions = {
                        AccountCircle(size = 50.dp) {
                            SelectedUserState.username = UserState.username
                            navController.navigate(Screens.UserProfile.route)
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Primary
                    ),
                    // disable user interaction with the top bar
                )
            }
        },
        content = { innerPadding ->
            BottomOverlay(
                sheetContent = {
                    if (UserState.isCommentClicked) {
                        Comments(navController)
                    } else if (UserState.isEllipsisClicked) {
                        LazyColumn {
                            items(50) {
                                ListItem { Text("Item $it") }
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
                            .padding(innerPadding),
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
        },
        floatingActionButton = {
            if (!sheetState.isVisible) {
                AddCircle {
                    navController.navigate(Screens.NewPost.route)
//                    focusRequester.showKeyboard(context)
                }
            }
        },
        bottomBar = {
            if (!sheetState.isVisible) {
                BottomBar(navController)
            }
        }
    )
}