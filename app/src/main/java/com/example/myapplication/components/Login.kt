package com.example.myapplication.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.myapplication.state.UserState

@Composable
fun Login() {
    val isClicked = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                TextInput(
                    value = UserState.userId,
                    label = "Username",
                    errorText = "Please enter your username",
                    isError = UserState.userId.isEmpty() && isClicked.value,
                    onValueChange = { UserState.userId = it })
                TextInput(
                    value = UserState.password,
                    label = "Password",
                    errorText = "Please enter your password",
                    isError = UserState.password.isEmpty() && isClicked.value,
                    onValueChange = { UserState.password = it },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    )
                )
            }

        }
    }
}