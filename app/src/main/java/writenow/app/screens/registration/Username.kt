package writenow.app.screens.registration

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.navigation.NavController
import org.json.JSONObject
import writenow.app.components.TextInput
import writenow.app.components.registration.RegistrationFooter
import writenow.app.components.registration.RegistrationLayout
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.utils.capitalizeFirstLetter
import writenow.app.utils.isValid

@Composable
fun Username(navController: NavController) {
//    var isRegistered by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }
    val jsonObject by remember {
        mutableStateOf(JSONObject().apply {
            put("firstName", capitalizeFirstLetter(UserState.firstName))
            put("lastName", capitalizeFirstLetter(UserState.lastName))
            put("email", UserState.email)
            put("passwordHash", UserState.password)
        })
    }

    val isValid = isValid("username")

//    LaunchedEffect(key1 = isClicked) {
//            Log.d("Clicked", "$isClicked")
//        if (isClicked) {
//            Log.d("Clicked", "$isClicked")
//            jsonObject.put("username", UserState.username)
//            isRegistered = Users.register(jsonObject)
//        }
//    }

    fun proceedToNextScreen() {
        isClicked = true

        jsonObject.put("username", UserState.username)

        if (!isValid.isValid) return

        Users.register(jsonObject) {
            if (it && UserState.username.isNotEmpty()) {
                UserState.isLoggedIn = true

                navController.navigate(Screens.Posts)
            } else {
                UserState.isLoggedIn = false
            }
        }
    }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = isValid.errorText,
            isError = !isValid.isValid && isClicked,
            onValueChange = {
                UserState.username = it

//                jsonObject.put("username", UserState.username)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
        )
        RegistrationFooter(btnText = "Let's get started!", onBtnClick = { proceedToNextScreen() })
    }
}