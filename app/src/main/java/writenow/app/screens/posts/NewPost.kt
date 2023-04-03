package writenow.app.screens.posts

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import writenow.app.components.post.PostBox
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.UserState

@Composable
fun NewPost(navController: NavController) {
    var value by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }

    fun post() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = JSONObject().apply {
                put("userID", UserState.id)
                put("postContents", value)
                put("visible", if (isPublic) 1 else 0)
            }

            Posts.post(json) { Log.d("NewPost", "post: $it") }
        }
    }

    PostBox(
        navController = navController,
        buttonText = "Post",
        placeholder = "New post",
        onBtnClick = {
            if (!UserState.hasPosted && (value.isNotEmpty() || value.isNotBlank())) {
                post()
                UserState.hasPosted = true
            }
            navController.navigate(Screens.Posts)
        },
        onValueChange = { value = it },
        onChipClick = { isPublic = it }
    )
}