package writenow.app.screens.profile.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import writenow.app.R
import writenow.app.components.ClickableRow
import writenow.app.components.dialogs.ActiveHoursDialog
import writenow.app.components.dialogs.activeday.ActiveDayDialog
import writenow.app.components.profile.Section
import writenow.app.components.profile.settings.SettingsLayout
import writenow.app.dbtables.Users
import writenow.app.state.GlobalState
import writenow.app.state.UserState
import writenow.app.utils.ActiveHours
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Notifications(navController: NavController) {
    val (isDaySelected, setIsDaySelected) = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val modalDialogActiveHoursState = rememberMaterialDialogState()
    val modalDialogStartTimeState = rememberMaterialDialogState()
    val modalDialogEndTimeState = rememberMaterialDialogState()

    val dayMap = mapOf(
        "su" to "Sunday",
        "m" to "Monday",
        "t" to "Tuesday",
        "w" to "Wednesday",
        "th" to "Thursday",
        "f" to "Friday",
        "s" to "Saturday"
    )

    val selectedDays by remember {
        derivedStateOf {
            UserState.selectedDays.map { dayMap[it] }.sortedBy {
                when (it) {
                    "Sunday" -> 0
                    "Monday" -> 1
                    "Tuesday" -> 2
                    "Wednesday" -> 3
                    "Thursday" -> 4
                    "Friday" -> 5
                    "Saturday" -> 6
                    else -> 7
                }
            }.filterNotNull().joinToString(", ")
        }
    }

    fun updateActiveTimes() {
        lifecycle.lifecycleScope.launch {
            val start = UserState.activeHours.start.ifEmpty { "" }
            val end = UserState.activeHours.end.ifEmpty { "" }

            GlobalState.userRepository.updateUser(
                GlobalState.user!!.copy(
                    activeHoursEnd = end,
                    activeHoursStart = start,
                    activeDays = if (UserState.selectedDays.isNotEmpty()) UserState.selectedDays.joinToString(
                        ","
                    ) else ""
                )
            )
            Users.updateActiveHours(UserState.id, ActiveHours(start, end))
        }
    }

    SettingsLayout(
        title = "Notification Preferences", navController = navController
    ) { innerPadding, _, _ ->
        LazyColumn(contentPadding = innerPadding) {
            item {
                Section("App notifications") {
                    Text(
                        text = "Stay tuned, more notification preferences are in the works!",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Section("Post notification preferences") {
                    ClickableRow(
                        key = "Define active days",
                        value = selectedDays,
                        chevron = false,
                        leadingIcon = painterResource(id = R.drawable.outline_date_range)
                    ) { modalDialogActiveHoursState.show() }
                    ClickableRow(
                        key = "Define active hours",
                        value = when {
                            UserState.activeHours.start.isNotEmpty() && UserState.activeHours.end.isNotEmpty() -> "${UserState.activeHours.start} - ${UserState.activeHours.end}"
                            UserState.activeHours.start.isNotEmpty() -> UserState.activeHours.start
                            UserState.activeHours.end.isNotEmpty() -> UserState.activeHours.end
                            else -> ""
                        },
                        chevron = false,
                        leadingIcon = painterResource(id = R.drawable.outline_schedule)
                    ) { modalDialogStartTimeState.show() }
                    Text(
                        text = when {
                            UserState.activeHours.start.isNotEmpty() && UserState.activeHours.end.isNotEmpty() -> "You will receive notifications for ${selectedDays.ifEmpty { "all days" }} from ${UserState.activeHours.start} to ${UserState.activeHours.end}"
                            UserState.activeHours.start.isNotEmpty() -> "You will receive notifications to post for all days from ${UserState.activeHours.start}"
                            UserState.activeHours.end.isNotEmpty() -> "You will receive notifications to post for all days until ${UserState.activeHours.end}"
                            else -> "You will receive notifications to post at a random time every day"
                        }, color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        when {
            modalDialogStartTimeState.showing -> {
                ActiveHoursDialog(dialogState = modalDialogStartTimeState,
                    title = "Select start time",
                    positiveText = "Set",
                    neutralText = "Clear",
                    positiveOnClick = { modalDialogEndTimeState.show() },
                    neutralOnClick = {
                        UserState.activeHours.start = ""

                        updateActiveTimes()
                        modalDialogEndTimeState.show()
                    }) {
                    UserState.activeHours.start = DateTimeFormatter.ofPattern("h:mm a").format(it)

                    updateActiveTimes()
                }
            }
            modalDialogEndTimeState.showing -> {
                ActiveHoursDialog(dialogState = modalDialogEndTimeState,
                    title = "Select end time",
                    positiveText = "Set",
                    neutralText = "Clear",
                    negativeText = "Back",
                    neutralOnClick = {
                        UserState.activeHours.end = ""

                        updateActiveTimes()
                    },
                    negativeOnClick = { modalDialogStartTimeState.show() }) {
                    UserState.activeHours.end = DateTimeFormatter.ofPattern("h:mm a").format(it)

                    updateActiveTimes()
                }
            }
            modalDialogActiveHoursState.showing -> {
                ActiveDayDialog(dialogState = modalDialogActiveHoursState, onDaySelected = {
                    setIsDaySelected(UserState.selectedDays.contains(it))

                    if (isDaySelected) {
                        UserState.selectedDays -= it
                    } else {
                        UserState.selectedDays += it
                    }

                    updateActiveTimes()
                }, additionalText = {
                    Text(
                        text = if (UserState.selectedDays.isEmpty()) "You will receive notifications for all days" else "You will receive notifications for $selectedDays",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }, neutralOnClick = {
                    UserState.selectedDays = emptySet()

                    updateActiveTimes()
                }, positiveOnClick = { updateActiveTimes() })
            }
        }
    }
}