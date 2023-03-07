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
import com.example.myapplication.state.UserState

@Composable
fun Login(navController: NavController) {
    var isClicked by remember { mutableStateOf(false) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun proceedToNextScreen() {
        isClicked = true

        if (UserState.username.isNotEmpty() && UserState.password.isNotEmpty()) {
            navController.navigate(Screens.Posts)
        }
    }

    RegistrationLayout(text = "Login") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = "Please enter your username",
            isError = UserState.username.isEmpty() && isClicked,
            onValueChange = { UserState.username = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = UserState.password,
            label = "Password",
            errorText = "Please enter your password",
            isError = UserState.password.isEmpty() && isClicked,
            onValueChange = { UserState.password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
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
            onBtnClick = { proceedToNextScreen() },
        )
    }
}