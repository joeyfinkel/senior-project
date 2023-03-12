package writenow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.R
import writenow.app.components.icons.More

@Composable
fun TopBar(
    title: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) leadingIcon()
        if (title != null) title()
        if (trailingIcon != null) trailingIcon()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(
    title: String? = null,
    hasEllipsis: Boolean = false,
    state: ModalBottomSheetState? = null,
    coroutineScope: CoroutineScope? = null,
    onBackClick: () -> Unit,
) {
    TopBar(
        leadingIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painterResource(id = R.drawable.chevron_left),
                    contentDescription = "Back",
                    modifier = Modifier.size(35.dp)
                )
            }
        },
        title = {
            if (title != null) Text(text = title, fontSize = 16.sp)
        },
        trailingIcon = {
            if (hasEllipsis) More { coroutineScope?.launch { state?.show() } }
            else Box(
                modifier = Modifier.width(
                    40.dp
                ),
                content = {}
            )
        }
    )
}

