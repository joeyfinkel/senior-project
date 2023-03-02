package com.example.myapplication.screens.registration

import android.util.Log
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.myapplication.screens.Screens
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.registration.RegistrationFooter
import com.example.myapplication.components.registration.RegistrationLayout
import com.example.myapplication.state.UserState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun Username(navController: NavController) {
    val isClicked = remember { mutableStateOf(false) }

    fun proceedToNextScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient.Builder().build()
            val body = FormBody.Builder()
                .add("firstName", UserState.firstName)
                .add("lastName", UserState.lastName)
                .add("email", UserState.email)
                .add("passwordHash", UserState.password)
                .add("username", UserState.username)
                .build()
            val req =
                Request.Builder()
                    .url("http://www.write-now.lesspopmorefizz.com")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build()

            try {
                val response = client.newCall(req).execute()
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("API Response", responseBody)
                }
            } catch (e: Exception) {
                Log.e("API Error", e.toString())
            }

        }

        // #TODO Add the user to the DB using the information from UserState
        navController.navigate(Screens.Posts.route)
    }

    RegistrationLayout(text = "Now, lets create a username") {
        TextInput(
            value = UserState.username,
            label = "Username",
            errorText = "Please enter your username",
            isError = UserState.username.isEmpty() && isClicked.value,
            onValueChange = { UserState.username = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { proceedToNextScreen() }),
        )
        RegistrationFooter(btnText = "Let's get started!", onBtnClick = { proceedToNextScreen() })
    }
}