package writenow.app.screens.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.utils.capitalizeFirstLetter
import writenow.app.utils.isValid

@Composable
fun Username(navController: NavController) {
    var isClicked by remember { mutableStateOf(false) }

    val isValid = isValid("username")

    fun proceedToNextScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = JSONObject().apply {
                put("firstName", capitalizeFirstLetter(UserState.firstName))
                put("lastName", capitalizeFirstLetter(UserState.lastName))
                put("email", UserState.email)
                put("passwordHash", UserState.password)
                put("username", UserState.username)
            }

            Users.register(json) {
                UserState.isLoggedIn = true
            }
        }
    }

    fun register() {
        isClicked = true

        if (isValid.isValid) {
            proceedToNextScreen()
            navController.navigate(Screens.Posts)
        }
    }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = isValid.errorText,
            isError = !isValid.isValid && isClicked,
            onValueChange = { UserState.username = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = { register() }),
        )
        RegistrationFooter(btnText = "Let's get started!", onBtnClick = { register() })
    }
}