package com.example.myapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.myapplication.Screen
import com.example.myapplication.components.registration.RegistrationFooter
import com.example.myapplication.components.registration.RegistrationLayout
import com.example.myapplication.state.UserState

@Composable
fun Login(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun proceedToNextScreen() {
        isClicked.value = true

        if (UserState.firstName.isNotEmpty() && UserState.lastName.isNotEmpty()) {
            navController.navigate(Screen.Posts.route)
        }
    }

    RegistrationLayout(text = "Login") {
        TextInput(
            value = UserState.userId,
            label = "Username",
            errorText = "Please enter your username",
            isError = UserState.userId.isEmpty() && isClicked.value,
            onValueChange = { UserState.userId = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = UserState.password,
            label = "Password",
            errorText = "Please enter your password",
            isError = UserState.password.isEmpty() && isClicked.value,
            onValueChange = { UserState.password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RegistrationFooter(
                btnText = "Login",
                additionalText = "Register",
                onTextClick = { navController.navigate(Screen.NameRegistration.route) },
                onBtnClick = { proceedToNextScreen() },
            )
        }
    }
}