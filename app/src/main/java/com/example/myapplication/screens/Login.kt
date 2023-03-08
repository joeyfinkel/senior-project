package com.example.myapplication.screens

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
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.registration.RegistrationFooter
import com.example.myapplication.components.registration.RegistrationLayout
import com.example.myapplication.dbtables.User
import com.example.myapplication.dbtables.Users
import com.example.myapplication.state.UserState

@Composable
fun Login(navController: NavController) {
    var errorText by remember { mutableStateOf("") }
    var isClicked by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    val users = remember { mutableListOf<User>() }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        users += Users.getAll()
    }

    fun login() {
        val user = users.find {
            it.username == UserState.username
                    && it.passwordHash == UserState.password
        }
        val isNotEmpty = UserState.username.isNotEmpty() && UserState.password.isNotEmpty()

        isClicked = true
        isError = true

        println(user)
        println(isNotEmpty)

        if (user != null && isNotEmpty) {
            UserState.isLoggedIn = true
            errorText = ""
            isError = false

            navController.navigate(Screens.Posts)
        }

        if (user == null && isNotEmpty) {
            isError = true
            errorText = "Incorrect username or password"
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