package com.example.myapplication.components.registration

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Information(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }
    val emailErrorText = remember { mutableStateOf("Please enter your email") }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun proceedToNextScreen() {
        val email = UserState.email
        isClicked.value = true

        println("email is valid: ${isValidEmail(email)}")

        if (isValidEmail(email)) {
            emailErrorText.value = "Please enter a valid email"
        }

        if (isValidEmail(email) && UserState.password.isNotBlank()) {
            println("Here")
            navController.navigate(Screen.UsernameRegistration.route)
        }
    }

    RegistrationLayout(text = "Continue with your email") {
        TextInput(
            value = UserState.email,
            label = "Email",
            errorText = "Please enter your email",
            isError = isValidEmail(UserState.email) && isClicked.value,
            onValueChange = { UserState.email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
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
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        RegistrationFooter(btnText = "Register", onBtnClick = { proceedToNextScreen() }
        )
    }
}

