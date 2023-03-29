package writenow.app.components.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import writenow.app.components.icons.AccountCircle
import writenow.app.screens.Screens
import writenow.app.state.SelectedUserState
import writenow.app.utils.defaultText
import writenow.app.utils.skeletonEffect

@Composable
private fun TextColumn(
    isClickable: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    username: @Composable () -> Unit,
    datePosted: (@Composable () -> Unit)? = null,
    postContents: @Composable () -> Unit,
) {
    val modifier =
        if (!isLoading) Modifier
            .clickable(enabled = isClickable, onClick = onClick)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress()
                    },
                    onTap = {
                        onClick()
                    }

                )
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
            username()
            datePosted?.invoke()
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
    text: String? = defaultText,
    datePosted: String,
    navController: NavController,
    onLongPress: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) = ContentRow(
    isLoading = false,
    circle = {
        AccountCircle(
            size = 35.dp, modifier = Modifier.align(Alignment.Top)
        ) {
            SelectedUserState.id = userId
            SelectedUserState.username = username

            navController.navigate(Screens.UserProfile)
        }
    },
    column = {
        TextColumn(isClickable = true,
            isLoading = false,
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
            postContents = {
                if (text != null) {
                    Text(
                        text = text,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            datePosted = {
                Text(
                    text = datePosted, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
)

///**
// * The content of the post. It will render out the who posted it and what they posted.
// * @param username The username of the user who posted.
// * @param text The contents of the post.
// */
//@Composable
//fun PostContent(
//    userId: Int,
//    username: String,
//    text: String? = defaultText,
//    datePosted: String,
//    onClickEnabled: Boolean = true,
//    isLoading: Boolean = false,
//    navController: NavController,
//    onClick: () -> Unit = {}
//) {
//    PostContent(
//        userId = userId,
//        username = username,
//        text = text,
//        onClickEnabled = onClickEnabled,
//        isLoading = isLoading,
//        navController = navController,
//        datePosted = datePosted,
//        onClick = onClick
//    )
//}

@Composable
fun LoadingPostContent() = ContentRow(isLoading = true, circle = {
    Box(
        modifier = Modifier
            .size(it)
            .clip(CircleShape)
            .skeletonEffect()
    )
}, column = {
    TextColumn(isClickable = false, isLoading = true, onClick = {}, onLongPress = {}, username = {
        Box(
            modifier = Modifier
                .height(15.dp)
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(16.dp))
                .skeletonEffect()
        )
    }, postContents = {
        Box(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .skeletonEffect()
        )
    })
})