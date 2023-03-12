package writenow.app.components.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import writenow.app.components.DefaultButton
import writenow.app.screens.Screens

@Composable
fun ProfileButton(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    borderRadius: Dp = 20.dp,
    navController: NavController? = null
) {
    var buttonText by remember { mutableStateOf("Follow") }

    DefaultButton(
        modifier = modifier,
        width = 100.dp,
        spacedBy = 25.dp,
        btnText = if (isEdit) buttonText else "Edit profile",
        icon = if (isEdit)
            if (buttonText == "Follow") Icons.Default.Add else Icons.Default.Check
        else null,
        borderRadius = borderRadius,
        onBtnClick = {
            if (!isEdit && navController != null)
                navController.navigate(Screens.EditProfile)
            else
                buttonText = if (buttonText == "Follow") "Following" else "Follow"
        }
    )
}