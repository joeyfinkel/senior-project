package writenow.app.screens

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.dbtables.Users
import writenow.app.state.UserState

@Composable
fun Login(navController: NavController) {
    var errorText by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isLoggedIn by remember { mutableStateOf(false) }

    val isClicked by remember { mutableStateOf(false) }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun login() {
        val isNotEmpty = UserState.username.isNotEmpty() && UserState.password.isNotEmpty()

        if (isNotEmpty) {
            Users.login(UserState.username, UserState.password) { isLoggedIn = it }

            if (isLoggedIn) {
                println("Logged in")
                UserState.isLoggedIn = true
                errorText = ""
                isError = false

                navController.navigate(Screens.Posts)
            } else {
                println("Incorrect username or password")
                isError = true
                errorText = "Incorrect username or password"
            }
        } else {
            isError = true
            errorText = "Please enter your username and password"
        }
    }

    RegistrationLayout(text = "Login") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = errorText.ifEmpty { "Please enter your username" },
            isError = isError && isClicked,
            onValueChange = { UserState.username = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = UserState.password,
            label = "Password",
            errorText = errorText.ifEmpty { "Please enter your password" },
            isError = isError && isClicked,
            onValueChange = { UserState.password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { login() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        RegistrationFooter(
            btnText = "Login",
            additionalText = "Don't have an account? Create an account",
            onTextClick = {
                UserState.username = ""
                UserState.password = ""

                navController.navigate(Screens.NameRegistration)
            },
            onBtnClick = { login() },
        )
    }
}