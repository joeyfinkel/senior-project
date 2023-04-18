package writenow.app.components.post

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.TopBar
import writenow.app.components.icons.AccountCircle
import writenow.app.state.UserState
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.placeholderColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostBox(
    navController: NavController,
    placeholder: String,
    onBtnClick: () -> Unit,
    onValueChange: (String) -> Unit,
    onChipClick: (Boolean) -> Unit
) {
    val (value, setValue) = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val darkMode = isSystemInDarkTheme()

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(leadingIcon = {
            ClickableText(text = AnnotatedString("Cancel"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                onClick = { navController.popBackStack() })
        }, trailingIcon = {
            IconButton(onClick = {
                if (value.isEmpty()) {
                    // show dialog here
                } else {
                    onBtnClick()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.new_post), contentDescription = "Post"
                )
            }
        })
        PostContainer(halfHeight = true) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AccountCircle(
                    size = 35.dp,
                    modifier = Modifier.align(Alignment.Top),
                    bitmap = UserState.bitmap
                )
                Column(
                    modifier = Modifier
                        .padding(bottom = 5.dp, end = 5.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Chips(values = listOf("Public", "Private"), onClick = {
                        UserState.isPostPrivate = it == 0

                        onChipClick(UserState.isPostPrivate)
                    })
                    TextField(
                        value = value,
                        placeholder = { Text(text = placeholder) },
                        onValueChange = {
                            setValue(it)
                            onValueChange(it)
                        },
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