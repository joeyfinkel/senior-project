package com.example.myapplication.components.registration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Username(onButtonClick: () -> Unit, onBackButtonClick: () -> Unit) {
    val navController = rememberNavController()
    val isClicked = remember { mutableStateOf(false) }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.userId,
            label = "Username",
            errorText = "Please enter your username",
            isError = UserState.userId.isEmpty() && isClicked.value,
            onValueChange = { UserState.userId = it }
        )
        RegistrationFooter(
            btnText = "Let's get started!",
            onBtnClick = onButtonClick
        )
    }
}