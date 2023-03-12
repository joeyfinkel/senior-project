package writenow.app.screens.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.navigation.NavController
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.screens.Screens
import writenow.app.state.UserState

@Composable
fun Names(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }

    fun proceedToNextScreen() {
        isClicked.value = true

        if (UserState.firstName.isNotEmpty() && UserState.lastName.isNotEmpty()) {
            navController.navigate(Screens.InformationRegistration)
        }
    }

    RegistrationLayout(text = "Enter your name") {
        TextInput(
            value = UserState.firstName,
            label = "First name",
            errorText = "Please enter your first name",
            isError = UserState.firstName.isEmpty() && isClicked.value,
            onValueChange = { UserState.firstName = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onNext = { focusRequester2.requestFocus() }),
            modifier = Modifier.focusRequester(focusRequester1)
        )
        TextInput(
            value = UserState.lastName,
            label = "Last name",
            errorText = "Please enter your last name",
            isError = UserState.lastName.isEmpty() && isClicked.value,
            onValueChange = { UserState.lastName = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
            modifier = Modifier.focusRequester(focusRequester2)
        )
        RegistrationFooter(
            btnText = "Next",
            additionalText = "Already have an account? Login here",
            onTextClick = {
                UserState.firstName = ""
                UserState.lastName = ""
                UserState.username = ""

                navController.navigate(Screens.Login)
            },
            onBtnClick = { proceedToNextScreen() },
        )
    }
}