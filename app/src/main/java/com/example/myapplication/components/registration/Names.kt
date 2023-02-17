package com.example.myapplication.components.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Screen
import com.example.myapplication.components.TextInput
import com.example.myapplication.state.UserState

@Composable
fun Names(onButtonClick: () -> Unit, onTextClick: () -> Unit) {
    val isClicked = remember { mutableStateOf(false) }

    RegistrationLayout(text = "Enter your name") {
        TextInput(value = UserState.firstName,
            label = "First name",
            errorText = "Please enter your first name",
            isError = UserState.firstName.isEmpty() && isClicked.value,
            onValueChange = { UserState.firstName = it })
        TextInput(value = UserState.lastName,
            label = "Last name",
            errorText = "Please enter your last name",
            isError = UserState.lastName.isEmpty() && isClicked.value,
            onValueChange = { UserState.lastName = it })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RegistrationFooter(
                btnText = "Next",
                additionalText = "Login",
                onTextClick = onTextClick,
                onBtnClick = {
                    isClicked.value = true

                    if (UserState.firstName.isNotEmpty() && UserState.lastName.isNotEmpty())
                        onButtonClick()
                },
            )
        }
    }
}