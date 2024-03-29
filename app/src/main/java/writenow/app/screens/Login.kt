package writenow.app.screens

import android.util.Log
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.data.entity.User
import writenow.app.dbtables.Posts
import writenow.app.dbtables.Users
import writenow.app.state.GlobalState
import writenow.app.state.UserState
import java.time.LocalDate
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(navController: NavController) {
    val (errorText, setErrorText) = remember { mutableStateOf("") }
    val (isError, setIsError) = remember { mutableStateOf(false) }
    val (isClicked, _) = remember { mutableStateOf(false) }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    suspend fun performLogin(): Boolean {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                val isNotEmpty = UserState.username.isNotEmpty() && UserState.password.isNotEmpty()

                Users.login(UserState.username, UserState.password) { loggedIn, user ->
                    if (isNotEmpty && loggedIn) {
                        UserState.isLoggedIn = true

                        setErrorText("")
                        setIsError(false)

                        if (user != null) {
                            UserState.id = user.id
                            UserState.username = user.username
                            UserState.email = user.email
                            UserState.firstName = user.firstName
                            UserState.lastName = user.lastName
                            UserState.activeHours.start = user.activeHours.start
                            UserState.activeHours.end = user.activeHours.end
                        }

                        continuation.resume(true)
                    } else {
                        UserState.isLoggedIn = false

                        setErrorText("Incorrect username or password")
                        setIsError(true)
                        Log.e("Logged in", "False")
                        continuation.resume(false)
                    }
                }
            }
        }
    }

    fun login() {
        keyboardController?.hide()

        CoroutineScope(Dispatchers.Main).launch {
            val isNotEmpty = UserState.username.isNotEmpty() && UserState.password.isNotEmpty()
            val date = LocalDate.now().dayOfMonth

            if (isNotEmpty) {
                val loggedIn = performLogin()

                if (loggedIn) {
                    UserState.getHasPosted()
                    navController.navigate(Screens.Posts)

                    if (date == Posts.getLastPostDate(UserState.id)) {
                        UserState.hasPosted = true
                    }

                    GlobalState.userRepository.addUser(
                        User(
                            uuid = UserState.id,
                            firstName = UserState.firstName,
                            lastName = UserState.lastName,
                            email = UserState.email,
                            password = UserState.password,
                            username = UserState.username,
                            displayName = UserState.displayName,
                            bio = UserState.bio,
                            activeDays = UserState.selectedDays.joinToString(","),
                            activeHoursStart = UserState.activeHours.start,
                            activeHoursEnd = UserState.activeHours.end,
                            hasPosted = if (UserState.hasPosted) 1 else 0,
                            isPostPrivate = if (UserState.isPostPrivate) 1 else 0,
                        )
                    )
                }
            } else {
                setIsError(true)
                setErrorText("Please enter your username and password")
            }
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