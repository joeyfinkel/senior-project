package writenow.app.screens.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.utils.isValid

@Composable
fun Information(navController: NavController) {
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    var isClicked by remember { mutableStateOf(false) }

    val isValidEmail = isValid("email")
    val isValidPassword = isValid("password", false)

    fun proceedToNextScreen() {
        isClicked = true

        if (isValidEmail.isValid) navController.navigate(Screens.UsernameRegistration)
    }

    RegistrationLayout(text = "Continue with your email") {
        TextInput(
            value = UserState.email,
            label = "Email",
            errorText = isValidEmail.errorText,
            isError = !isValidEmail.isValid && isClicked,
            onValueChange = { UserState.email = it.trim() },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = UserState.password,
            label = "Password",
            errorText = isValidPassword.errorText,
            isError = !isValidPassword.isValid && isClicked,
            onValueChange = { UserState.password = it },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        RegistrationFooter(btnText = "Register", onBtnClick = { proceedToNextScreen() })
    }
}

