package com.example.myapplication.screens.registration

import android.util.Log
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.registration.RegistrationFooter
import com.example.myapplication.components.registration.RegistrationLayout
import com.example.myapplication.dbtables.Users
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.UserState
import com.example.myapplication.utils.capitalizeFirstLetter
import com.example.myapplication.utils.isValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

@Composable
fun Username(navController: NavController) {
    val isValid = isValid("username")

    val isClicked by remember { mutableStateOf(false) }

    fun insertUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val json = JSONObject().apply {
                put("firstName", capitalizeFirstLetter(UserState.firstName))
                put("lastName", capitalizeFirstLetter(UserState.lastName))
                put("email", UserState.email)
                put("passwordHash", UserState.password)
                put("username", UserState.username)
            }
            val inserted = Users.insert(json)

            Log.d("Inserted", inserted.toString())
        }
    }

    fun proceedToNextScreen() {
        if (isValid.isValid) {
            insertUser()
            UserState.isLoggedIn = true
            navController.navigate(Screens.Posts)
        }
    }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = isValid.errorText,
            isError = !isValid.isValid && isClicked,
            onValueChange = { UserState.username = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
        )
        RegistrationFooter(btnText = "Let's get started!", onBtnClick = { proceedToNextScreen() })
    }
}