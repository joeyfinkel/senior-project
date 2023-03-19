package writenow.app.components.icons

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.json.JSONObject
import writenow.app.dbtables.LikedPost
import writenow.app.dbtables.LikesAndComments
import writenow.app.state.PostState
import writenow.app.state.UserState

@Composable
private fun Like(postId: Int) {
    val filled = Icons.Default.Favorite
    val border = Icons.Default.FavoriteBorder
    val surfaceColor = MaterialTheme.colorScheme.onSurface

    var likedPost by remember { mutableStateOf<LikedPost?>(null) }
    var color by remember { mutableStateOf(surfaceColor) }
    var icon by remember { mutableStateOf(border) }

    LaunchedEffect(likedPost) {
        likedPost = PostState.likedPosts.firstOrNull { it.postId == postId }
        color = if (likedPost?.liked == 1) Color.Red else surfaceColor
        icon = if (likedPost?.liked == 1) filled else border
    }

    fun toggleLike() {
        // The user did not like the post, so we need to add it to the database
        if (likedPost == null) {
            Log.d("Like", "toggleLike: likedPost is null")

            val jsonObject = JSONObject().apply {
                put("postID", postId)
                put("userID", UserState.id)
                put("isLike", 1)
            }

            LikesAndComments.toggleLikeState(false, jsonObject) { success ->
                if (success) {
                    color = if (color == Color.Red) surfaceColor else Color.Red
                    icon = if (icon == filled) border else filled
                }
            }

            // add the post to the liked posts list if it doesn't exist
            PostState.likedPosts.add(LikedPost(postId, UserState.id, 1))
        }
        // The user DID like the post but now wants to unlike it
        else {
            val jsonObject = JSONObject().apply {
                put("postID", postId)
                put("userID", UserState.id)
                put("isLike", 0)
            }

            LikesAndComments.toggleLikeState(true, jsonObject) { success ->
                if (success) {
                    color = if (color == Color.Red) surfaceColor else Color.Red
                    icon = if (icon == filled) border else filled
                }
            }

            // remove the post from the liked posts list if it exists
            PostState.likedPosts.removeIf { it.postId == postId }
        }
    }

    Box(modifier = Modifier
        .size(24.dp)
        .clickable { toggleLike() }) {
        IconButton(
            onClick = { toggleLike() }, modifier = Modifier.padding(bottom = 0.dp)
        ) {
            Icon(imageVector = icon, contentDescription = "Like", tint = color)
        }
    }
}

@Composable
fun Like(
    postId: Int,
    label: String,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
) = IconWithLabel(modifier = modifier,
    horizontalAlignment = horizontalAlignment,
    verticalArrangement = verticalArrangement,
    icon = { Like(postId = postId) },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) })

@Composable
fun Like(
    postId: Int,
    label: String,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
) = IconWithLabel(modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
    icon = { Like(postId = postId) },
    label = { Text(text = label, color = MaterialTheme.colorScheme.onSurface) },
    onClick = { })

