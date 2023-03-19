package writenow.app.screens.profile.editprofile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.ClickableRow
import writenow.app.components.icons.AccountCircle
import writenow.app.components.profile.*
import writenow.app.screens.Screens
import writenow.app.state.UserState
import writenow.app.ui.theme.PersianOrange
import writenow.app.ui.theme.PlaceholderColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfile(navController: NavController) {
    var bio by remember { mutableStateOf("") }
    val darkMode = isSystemInDarkTheme()

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
                    Text(text = "Change photo", color = MaterialTheme.colorScheme.onSurface)
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
                        Text(text = "Bio", color = MaterialTheme.colorScheme.onSurface)
                        OutlinedTextField(
                            value = UserState.bio,
                            placeholder = {
                                if (UserState.bio.isEmpty() || UserState.bio.isBlank())
                                    Text(text = "Add some more info about yourself")
                            },
                            modifier = Modifier.fillMaxSize(),
                            onValueChange = { UserState.bio = it },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurface,
                                placeholderColor = PlaceholderColor(darkMode),
                                trailingIconColor = PlaceholderColor(darkMode),
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = PersianOrange,
                                unfocusedBorderColor = PersianOrange,
                                cursorColor = PersianOrange
                            ),
                            trailingIcon = { Text(text = "${UserState.bio.length}/100") }
                        )
                    }
                }
            }
        }
    }
}
