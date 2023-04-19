package writenow.app.screens.profile.editprofile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.TextInput
import writenow.app.components.profile.ProfileLayout
import writenow.app.components.profile.Section
import writenow.app.state.SelectedUserState
import writenow.app.state.UserState

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditBirthday(navController: NavController) {
    ProfileLayout(title = "Edit birthday",
        navController = navController,
        onBackClick = { navController.popBackStack() }) { _, _ ->
        item {
            Section(columnSpacing = 0.dp) {
                TextInput(value = UserState.birthday,
                    label = "Birthday",
                    spacer = false,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        UserState.birthday = it
                        SelectedUserState.birthday = it
                    })
            }
        }
    }
}