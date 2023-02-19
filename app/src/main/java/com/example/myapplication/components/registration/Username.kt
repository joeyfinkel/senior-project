package com.example.myapplication.components.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Username(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }

    fun proceedToNextScreen() {
        // #TODO Add the user to the DB using the information from UserState
        navController.navigate(Screen.Posts.route)
    }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.userId,
            label = "Username",
            errorText = "Please enter your username",
            isError = UserState.userId.isEmpty() && isClicked.value,
            onValueChange = { UserState.userId = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
        )
        RegistrationFooter(
            btnText = "Let's get started!",
            onBtnClick = { proceedToNextScreen() }
        )
    }
}