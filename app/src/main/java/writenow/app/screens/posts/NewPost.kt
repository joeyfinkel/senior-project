package writenow.app.screens.posts

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import writenow.app.components.DefaultButton
import writenow.app.components.TopBar
import writenow.app.components.icons.AccountCircle
import writenow.app.components.post.Chips
import writenow.app.components.post.PostContainer
import writenow.app.dbtables.Posts
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewPost(navController: NavController) {
    var value by remember { mutableStateOf("") }
    var isPublic by remember { mutableStateOf(true) }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val darkMode = isSystemInDarkTheme()

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

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            leadingIcon = {
                ClickableText(
                    text = AnnotatedString("Cancel"),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = { navController.popBackStack() }
                )
            },
            trailingIcon = {
                DefaultButton(btnText = "Post",
                    enabled = value.isNotEmpty() || value.isNotBlank(),
                    onBtnClick = {
                        if (!UserState.hasPosted && (value.isNotEmpty() || value.isNotBlank())) {
                            post()
                            UserState.hasPosted = true
                        }
                        navController.navigate(Screens.Posts)
                    })
            }
        )
        PostContainer(halfHeight = true) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                //
                AccountCircle(size = 35.dp, modifier = Modifier.align(Alignment.Top))
                Column(
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 5.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Chips(values = listOf("Public", "Private"), onClick = { isPublic = it == 0 })
                    TextField(
                        value = value,
                        placeholder = { Text(text = "New Post") },
                        onValueChange = { value = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .focusRequester(focusRequester)
                            .onFocusChanged {
                                if (it.isFocused) {
                                    Log.d("NewPost", "Focused")
                                    keyboard?.show()
                                }
                            },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            placeholderColor = placeholderColor(darkMode),
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                            backgroundColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = PersianOrange
                        )
                    )
                }
            }
        }
    }
}