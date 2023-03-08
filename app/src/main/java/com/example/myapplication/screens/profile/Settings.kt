package com.example.myapplication.screens.profile

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.components.ClickableRow
import com.example.myapplication.components.profile.BottomOverlayButton
import com.example.myapplication.components.profile.LogoutButton
import com.example.myapplication.components.profile.ProfileLayout
import com.example.myapplication.components.profile.Section
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(navController: NavController) {
    ProfileLayout(
        title = "Settings",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) }
    ) { innerPadding, _, _ ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            // Account section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Account") {
                    ClickableRow(
                        key = "Account",
                        trailingText = false,
                        leadingIcon = Icons.Filled.Person,
                        onClick = {}
                    )
                    ClickableRow(
                        key = "Privacy",
                        trailingText = false,
                        leadingIcon = painterResource(id = R.drawable.shield),
                        onClick = {}
                    )
                }
            }
            // Support section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Support") {
                    ClickableRow(
                        key = "Help",
                        leadingIcon = painterResource(id = R.drawable.help),
                        onClick = {}
                    )
                    ClickableRow(
                        key = "About",
                        leadingIcon = Icons.Filled.Info,
                        onClick = {}
                    )
                    ClickableRow(
                        key = "Report a problem",
                        leadingIcon = painterResource(id = R.drawable.report),
                        onClick = {}
                    )
                }
            }
            // Login section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Login") {
                    ClickableRow(
                        key = "Switch account",
                        leadingIcon = painterResource(id = R.drawable.switch_account),
                        onClick = {}
                    )
                    LogoutButton(navController = navController)
                }
            }
        }
    }
}