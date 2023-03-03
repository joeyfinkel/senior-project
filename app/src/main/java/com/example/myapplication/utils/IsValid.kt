package com.example.myapplication.utils

import android.util.Patterns
import androidx.compose.runtime.*
import com.example.myapplication.dbtables.Users
import com.example.myapplication.state.UserState

data class IsValid(val isValid: Boolean, val errorText: String)

@Composable
fun isValid(prop: String, check: Boolean = true): IsValid {
    var list by remember { mutableStateOf(emptyList<String>()) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (check) list = Users.get(prop)
    }

    fun returnFn(): Boolean {
        val value = UserState[prop] as String

        if (value.isBlank() || value.isEmpty()) {
            text = "Please enter your $prop"

            return false
        }

        if (check) {
            if (value in list) {
                text = "${capitalizeFirstLetter(prop)} already exists"

                return false
            }
        }

        if (prop == "email") {
            if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                text = "Please enter a valid email"

                return false
            }
        }

        return true
    }

    return IsValid(returnFn(), text)
}