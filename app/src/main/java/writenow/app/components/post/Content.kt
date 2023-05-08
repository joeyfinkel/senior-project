package writenow.app.components.post

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState
import writenow.app.utils.LaunchedEffectOnce
import writenow.app.utils.defaultText
import writenow.app.utils.skeletonEffect

@Composable
private fun TextColumn(
    isClickable: Boolean,
    isLoading: Boolean,
    isEdited: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    username: @Composable () -> Unit,
    question: @Composable () -> Unit,
    datePosted: (@Composable () -> Unit)? = null,
    postContents: @Composable () -> Unit,
) {
    val modifier = if (!isLoading) Modifier
        .clickable(enabled = isClickable, onClick = onClick)
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                onLongPress()
            }, onTap = {
                onClick()
            })
        } else Modifier

    Column(
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                username()
                question()
            }
            if (isEdited) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_edit),
                        contentDescription = "Edited",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(15.dp)
                    )
                    datePosted?.invoke()
                }
            } else {
                datePosted?.invoke()
            }
        }
        Spacer(modifier = Modifier.height(if (isLoading) 10.dp else 5.dp))
        postContents()
    }
}

@Composable
private fun ContentRow(
    isLoading: Boolean, circle: @Composable (RowScope.(Dp) -> Unit), column: @Composable () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    circle(35.dp)
    Spacer(modifier = Modifier.width(if (isLoading) 10.dp else 5.dp))
    column()
}

@Composable
fun PostContent(
    userId: Int,
    username: String,
    question: String,
    isEdited: Boolean = false,
    text: String? = defaultText,
    datePosted: String,
    navController: NavController,
    onLongPress: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val (isLoading, setIsLoading) = remember { mutableStateOf(false) }
    val (bitmap, setBitmap) = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffectOnce {
        if (userId == SelectedUserState.id) {
            setIsLoading(true)
            setBitmap(UserState.bitmap)
            setIsLoading(false)
        }
    }

    ContentRow(isLoading = false, circle = {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .size(it)
                    .clip(CircleShape)
                    .skeletonEffect()
            )
        } else {
            AccountCircle(size = it,
                modifier = Modifier.align(Alignment.Top),
                bitmap = bitmap,
                onClick = if (SelectedUserState.id == UserState.id) null else {
                    {
                        SelectedUserState.id = userId
                        SelectedUserState.username = username

                        navController.navigate(Screens.UserProfile)
                    }
                })
        }
    }, column = {
        TextColumn(isClickable = true,
            isLoading = false,
            isEdited = isEdited,
            onClick = { if (onClick != null) onClick() },
            onLongPress = { if (onLongPress != null) onLongPress() },
            username = {
                Text(
                    text = username,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            question = {
                Log.d("question", question)
                if (question.isNotBlank()) {
                    Text(
                        text = question,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            postContents = {
                if (text != null) {
                    Text(
                        text = text,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            datePosted = {
                Text(
                    text = datePosted, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface
                )
            })
    })
}

@Composable
fun LoadingPostContent() = ContentRow(isLoading = true, circle = {
    Box(
        modifier = Modifier
            .size(it)
            .clip(CircleShape)
            .skeletonEffect()
    )
}, column = {
    TextColumn(
        isClickable = false,
        isLoading = true,
        isEdited = false,
        onClick = {},
        onLongPress = {},
        question = {},
        username = {
            Box(
                modifier = Modifier
                    .height(15.dp)
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(16.dp))
                    .skeletonEffect()
            )
        },
        postContents = {
            Box(
                modifier = Modifier
                    .height(75.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .skeletonEffect()
            )
        })
})