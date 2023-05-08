package writenow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.R
import writenow.app.components.bottom.bar.BottomBar
import writenow.app.components.bottom.overlay.BottomOverlay
import writenow.app.components.bottom.overlay.BottomOverlayButtonContainer
import writenow.app.components.bottom.overlay.comments.Comments
import writenow.app.components.icons.AccountCircle
import writenow.app.components.post.SelectedPost
import writenow.app.components.profile.BottomOverlayButton
import writenow.app.data.entity.User
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.GlobalState
import writenow.app.state.PostState
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.ui.theme.DefaultRadius
import writenow.app.ui.theme.PersianOrange

@Composable
private fun TopBar(
    title: String, navController: NavController, lazyListState: LazyListState, scope: CoroutineScope
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val (selectedIdx, setSelectedIdx) = remember { mutableStateOf(0) }

    LaunchedEffect(selectedIdx) {
        if (selectedIdx == 0) {
            PostState.fetchNewPosts(UserState.hasPosted, UserState.id)
        } else {
            PostState.fetchNewPosts(UserState.hasPosted, UserState.id, false)
        }
    }

    Surface(
        color = PersianOrange,
        shape = RoundedCornerShape(bottomEnd = DefaultRadius, bottomStart = DefaultRadius)
    ) {
        SmallTopAppBar(
            title = {
                Column(
                    modifier = Modifier
                        .height(65.dp)
                        .padding(vertical = 5.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    ClickableText(text = AnnotatedString(title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = { scope.launch { lazyListState.scrollToItem(0) } })
                    Chips(
                        values = listOf("Feed", "Discover"),
                        activeBackgroundColor = surfaceColor,
                        disabledBackgroundColor = Color.Transparent,
                        enabled = UserState.hasPosted,
                        textColor = PersianOrange,
                        onClick = {
                            PostState.allPosts.clear()
                            setSelectedIdx(it)
//                            scope.launch { lazyListState.scrollToItem(0) }
                        },
                    )
                }
            },
            actions = {
                AccountCircle(size = 50.dp, bitmap = UserState.bitmap) {
                    SelectedUserState.id = UserState.id
                    SelectedUserState.username = UserState.username

                    navController.navigate(Screens.UserProfile)
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = PersianOrange
            ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun Layout(
    navController: NavController,
    lazyListState: LazyListState? = null,
    topBar: @Composable (scope: CoroutineScope) -> Unit,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    var deletedPost by remember { mutableStateOf(false) }

    val (isReporting, setIsReporting) = remember { mutableStateOf(false) }
    val (hasReported, setHasReported) = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val totalChildren = if (UserState.selectedPost?.uuid == UserState.id) 2 else 1
    val maxHeight = when {
        UserState.isEllipsisClicked -> totalChildren.toFloat() * 0.07
        UserState.isCommentClicked -> 0.5
        UserState.isPostClicked -> 1.5
        isReporting -> 0.56
        hasReported -> 0.2
        else -> 1.0
    }

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            UserState.selectedPost = null
            UserState.isEllipsisClicked = false
            UserState.isCommentClicked = false
            UserState.isPostClicked = false
        }
    }

    LaunchedEffect(deletedPost) {
        if (deletedPost) {
            GlobalState.userRepository.updateUser(
                User(
                    uuid = UserState.id,
                    firstName = UserState.firstName,
                    lastName = UserState.lastName,
                    username = UserState.username,
                    email = UserState.email,
                    password = UserState.password,
                    displayName = UserState.displayName,
                    bio = UserState.bio,
                    activeDays = UserState.selectedDays.toString(),
                    activeHoursStart = UserState.activeHours.start,
                    activeHoursEnd = UserState.activeHours.end,
                    hasPosted = 0,
                    isPostPrivate = 0,
                )
            )
            Posts.update(PostState.allPosts)
            scope.launch { sheetState.hide() }
        }

        deletedPost = false
    }

    Scaffold(topBar = { topBar(scope) }, bottomBar = {
        BottomBar(
            navController = navController,
            modifier = Modifier.zIndex(1f),
            scope = scope,
            lazyListState = lazyListState
        )
    }, contentColor = MaterialTheme.colorScheme.onSurface, content = { innerPadding ->
        BottomOverlay(sheetContent = {
            when {
                UserState.isCommentClicked -> Comments(navController)
                UserState.isEllipsisClicked -> BottomOverlayButtonContainer {
                    if (UserState.selectedPost?.uuid == UserState.id) {
                        BottomOverlayButton(icon = painterResource(id = R.drawable.outline_edit),
                            text = "Edit post",
                            onClick = { UserState.editPost(navController, scope, sheetState) })
                        BottomOverlayButton(
                            icon = Icons.Default.Delete,
                            text = "Delete",
                            color = Color.Red,
                            onClick = {
                                if (UserState.selectedPost?.id != null) {
                                    val id = UserState.selectedPost?.id!!

                                    Posts.delete(id) {
                                        if (it) {
                                            deletedPost = true
                                            UserState.hasPosted = false

                                            PostState.allPosts.removeAt(PostState.allPosts.indexOfFirst { post -> post.id == id })
                                        }
                                    }
                                }
                            })
                    } else {
                        BottomOverlayButton(icon = painterResource(id = R.drawable.report),
                            text = "Report",
                            onClick = {
                                UserState.isEllipsisClicked = false

                                setIsReporting(true)
                            })
                    }
                }
                UserState.isPostClicked -> SelectedPost(
                    scope = scope, sheetState = sheetState, navController = navController
                )
                isReporting -> BottomOverlayButtonContainer {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                setIsReporting(false)

                                UserState.isEllipsisClicked = true
                            }, modifier = Modifier
                                .size(24.dp)
                                .padding(0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_left),
                                contentDescription = "Back",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    GlobalState.reportReasons?.forEach {
                        BottomOverlayButton(text = it.reason) {
                            setIsReporting(false)
                            setHasReported(true)
                        }
                    }
                }
                hasReported -> BottomOverlayButtonContainer(verticalArrangement = Arrangement.SpaceAround) {
                    Text(
                        text = "Thank you for reporting this post!",
                        fontSize = 24.sp,
                    )
                    Text(
                        text = "We will review it shortly.",
                        fontSize = 16.sp,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultButton(btnText = "Back to post") {
                            setHasReported(false)
                            scope.launch { sheetState.hide() }
                        }
                    }
                }
            }
        },
            maxHeight = maxHeight,
            sheetState = sheetState,
            modifier = Modifier.zIndex(2f),
            content = {
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
                            content(sheetState, scope)
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            })
    })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    title: String,
    navController: NavController,
    lazyListState: LazyListState,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) {
    Layout(
        navController = navController, topBar = {
            TopBar(
                title = title,
                navController = navController,
                lazyListState = lazyListState,
                scope = it,
            )
        }, content = content
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Layout(
    navController: NavController,
    content: @Composable (sheetState: ModalBottomSheetState, scope: CoroutineScope) -> Unit
) = Layout(
    navController = navController, topBar = { }, content = content
)