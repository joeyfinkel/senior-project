package writenow.app.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import writenow.app.R
import writenow.app.components.icons.More
import writenow.app.state.UserState

@Composable
fun TopBar(
    title: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(horizontal = 2.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
) {
    if (leadingIcon != null) leadingIcon()
    if (title != null) title()
    if (trailingIcon != null) trailingIcon()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(
    title: String? = null,
    hasEllipsis: Boolean = false,
    state: ModalBottomSheetState? = null,
    coroutineScope: CoroutineScope? = null,
    onBackClick: () -> Unit,
) = TopBar(leadingIcon = {
    IconButton(onClick = onBackClick, modifier = Modifier.size(24.dp)) {
        Icon(
            painterResource(id = R.drawable.chevron_left),
            contentDescription = "Back",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}, title = {
    if (title != null) Text(
        text = title,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
    )
}, trailingIcon = {
    if (hasEllipsis) More {
        coroutineScope?.launch {
            UserState.isProfileEllipsisClicked = true

            state?.show()
        }
    }
    else Box(modifier = Modifier.width(40.dp), content = {})
})

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun TopBarPreview() {
    TopBar(
        title = "Title",
        hasEllipsis = true,
        onBackClick = { },
        state = null,
        coroutineScope = null,
    )
}


