package com.example.myapplication.components.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Information(onButtonClick: () -> Unit, onBackButtonClick: () -> Unit) {
    val navController = rememberNavController()
    val isClicked = remember { mutableStateOf(false) }

    RegistrationLayout(
        text = "Continue with your email",
        hasIcon = true,
        onIconClick = onBackButtonClick
    ) {
        TextInput(
            value = UserState.email,
            label = "Email",
            errorText = "Please enter your email",
            isError = UserState.email.isEmpty() && isClicked.value,
            onValueChange = { UserState.email = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )
        TextInput(
            value = UserState.password,
            label = "Password",
            errorText = "Please enter your password",
            isError = UserState.password.isEmpty() && isClicked.value,
            onValueChange = { UserState.password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
        RegistrationFooter(
            btnText = "Register",
            onBtnClick = {
                isClicked.value = true

                if (UserState.email.isNotEmpty() && UserState.password.isNotEmpty())
                    onButtonClick()
            }
        )
    }
}

