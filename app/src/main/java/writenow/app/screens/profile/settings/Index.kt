package writenow.app.screens.profile.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import writenow.app.R
import writenow.app.components.ClickableRow
import writenow.app.components.profile.LogoutButton
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.Section
import writenow.app.dbtables.Users
import writenow.app.screens.Screens
import writenow.app.state.GlobalState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(navController: NavController) {
    val (toggled, setToggled) = remember { mutableStateOf(false) }

    fun togglePostPrivacy(flag: Boolean? = null) {
        UserState.isPostPrivate = flag ?: !UserState.isPostPrivate

        Users.toggleDefaultPostVisibility(UserState.id, if (UserState.isPostPrivate) 0 else 1)
        setToggled(!toggled)
    }

    LaunchedEffect(key1 = UserState.isPostPrivate) {
        GlobalState.userRepository.updateUser(GlobalState.user!!.copy(isPostPrivate = if (UserState.isPostPrivate) 0 else 1))
        Log.d("Settings", "UserState.isPostPrivate: ${UserState.isPostPrivate}")
    }

    ProfileLayout(title = "Settings",
        navController = navController,
        onBackClick = { navController.navigate(Screens.UserProfile) },
        content = { _, _ ->
            // Account section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Account") {
                    ClickableRow(
                        key = "Account", leadingIcon = Icons.Filled.Person
                    ) { navController.navigate(Screens.AccountSettings) }
                    ClickableRow(
                        key = "Privacy", leadingIcon = painterResource(id = R.drawable.shield)
                    ) { navController.navigate(Screens.Privacy) }
                }
            }
            // Notifications section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Notifications") {
                    ClickableRow(
                        key = "Notifications", leadingIcon = Icons.Filled.Notifications
                    ) { navController.navigate(Screens.NotificationsSettings) }
                }
            }
            // Posts section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Posts") {
                    ClickableRow(
                        key = "Deleted posts",
                        leadingIcon = painterResource(id = R.drawable.outline_archive)
                    ) { navController.navigate(Screens.DeletedPosts) }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        ClickableRow(key = "New posts default to public",
                            leadingIcon = painterResource(id = R.drawable.new_post),
                            trailingIcon = {
                                Switch(
                                    checked = UserState.isPostPrivate,
                                    onCheckedChange = { togglePostPrivacy(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedIconColor = MaterialTheme.colorScheme.onSurface,
                                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.5f
                                        ),
                                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                                        uncheckedBorderColor = MaterialTheme.colorScheme.onSurface,
                                    )
                                )
                            }) { togglePostPrivacy() }
                        Text(
                            text = if (!UserState.isPostPrivate) "Only your followers will be able to see your new posts" else "Your new posts will be discoverable by everyone",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            // Support section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Support") {
                    ClickableRow(
                        key = "Help", leadingIcon = painterResource(id = R.drawable.help)
                    ) {}
                    ClickableRow(key = "About", leadingIcon = Icons.Filled.Info) {}
                    ClickableRow(
                        key = "Report a problem",
                        leadingIcon = painterResource(id = R.drawable.report)
                    ) {}
                }
            }
            // Login section
            item {
                Spacer(modifier = Modifier.height(25.dp))
                Section(title = "Login") {
                    ClickableRow(
                        key = "Switch account",
                        leadingIcon = painterResource(id = R.drawable.switch_account)
                    ) {}
                    LogoutButton(navController = navController)
                }
            }
        },
        additionalContent = {
            if (toggled)
                Snackbar(modifier = Modifier.padding(16.dp), action = {
                    Text(
                        text = "Dismiss",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }, content = {
                    Text(
                        text = "You can change your username and profile picture in the Account section",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                })
        })
}

@Preview
@Composable
fun SettingsPreview() {
    Settings(navController = NavController(LocalContext.current))
}