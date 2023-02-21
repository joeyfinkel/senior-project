package com.example.myapplication.components.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Names(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    var (firstName, lastName) = UserState

    fun proceedToNextScreen() {
        isClicked.value = true

        if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            navController.navigate(Screen.InformationRegistration.route)
        }
    }

    RegistrationLayout(text = "Enter your name") {
        TextInput(
            value = firstName,
            label = "First name",
            errorText = "Please enter your first name",
            isError = firstName.isEmpty() && isClicked.value,
            onValueChange = { firstName = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = lastName,
            label = "Last name",
            errorText = "Please enter your last name",
            isError = lastName.isEmpty() && isClicked.value,
            onValueChange = { lastName = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        RegistrationFooter(
            btnText = "Next",
            additionalText = "Already have an account? Login here",
            onTextClick = { navController.navigate(Screen.Login.route) },
            onBtnClick = { proceedToNextScreen() },
        )
    }
}