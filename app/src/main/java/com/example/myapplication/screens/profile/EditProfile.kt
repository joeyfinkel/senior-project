package com.example.myapplication.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.components.ClickableRow
import com.example.myapplication.components.icons.AccountCircle
import com.example.myapplication.components.profile.*
import com.example.myapplication.screens.Screens
import com.example.myapplication.state.UserState
import com.example.myapplication.ui.theme.Primary

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfile(navController: NavController) {
    var bio by remember { mutableStateOf("") }

    ProfileLayout(
        title = "Edit profile",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) }
    ) { innerPadding, _, _ ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AccountCircle(size = 75.dp)
                    Text(text = "Change photo")
                }
            }
            item { Spacer(modifier = Modifier.height(25.dp)) }
            item {
                Section(title = "About you") {
                    ClickableRow(
                        key = "Name",
                        value = UserState.displayName,
                        onClick = { navController.navigate(Screens.EditName) },
                    )
                    ClickableRow(
                        key = "Username",
                        value = UserState.username,
                        onClick = { /*TODO*/ })
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = "Bio")
                        OutlinedTextField(
                            value = UserState.bio,
                            placeholder = {
                                if (UserState.bio.isEmpty() || UserState.bio.isBlank())
                                    Text(text = "Add some more info about yourself")
                            },
//                            maxLines = 5,
                            modifier = Modifier.fillMaxSize(),
                            onValueChange = { UserState.bio = it },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = Primary,
                                cursorColor = MaterialTheme.colors.primary
                            ),
                            trailingIcon = { Text(text = "${UserState.bio.length}/100") }
                        )
                    }
                }
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 15.dp),
//                    horizontalAlignment = Alignment.Start,
//                    verticalArrangement = Arrangement.spacedBy(5.dp)
//                ) {
//                    Text(
//                        text = "About you",
//                        style = MaterialTheme.typography.caption,
//                        color = Color.Gray
//                    )
//                    Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
//                        ClickableRow(
//                            key = "Name",
//                            value = UserState.displayName
//                        )
//                        ClickableRow(key = "Username", value = UserState.username)
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(150.dp)
//                        ) {
//                            Text(text = "Bio")
//                            OutlinedTextField(
//                                value = bio,
//                                placeholder = {
//                                    if (UserState.bio.isEmpty() || UserState.bio.isBlank()) Text(
//                                        text = "Add some more info about yourself",
//                                    )
//                                },
//                                modifier = Modifier.fillMaxSize(),
//                                onValueChange = {
//                                    bio = it
//                                    UserState.bio = bio
//                                },
//                                colors = TextFieldDefaults.outlinedTextFieldColors(
//                                    focusedBorderColor = Primary,
//                                    unfocusedBorderColor = Primary,
//                                    cursorColor = MaterialTheme.colors.primary
//                                )
//                            )
//                        }
//                    }
//                }
            }
        }
    }
}
