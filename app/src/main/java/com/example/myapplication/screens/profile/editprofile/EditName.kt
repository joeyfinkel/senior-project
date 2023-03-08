package com.example.myapplication.screens.profile.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.TextInput
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.Section
import com.example.myapplication.state.SelectedUserState
import com.example.myapplication.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditName(navController: NavController) {
    ProfileLayout(
        title = "Edit name",
        navController = navController,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding, _, _ ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Section(columnSpacing = 0.dp) {
                TextInput(
                    value = UserState.displayName,
                    label = "Display name",
                    hint = "This is how your name will appear to others",
                    spacer = false,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        UserState.displayName = it
                        SelectedUserState.displayName = it
                    }
                )
            }
        }
    }
}