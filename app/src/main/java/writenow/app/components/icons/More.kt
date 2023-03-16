package writenow.app.components.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import writenow.app.R

@Composable
fun More(onClick: () -> Unit) {
    val svg = painterResource(id = R.drawable.outline_more_horiz_24)
    val surfaceColor = MaterialTheme.colorScheme.surface

    IconWithLabel(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        icon = {
            Box(modifier = Modifier
                .size(24.dp)
                .clickable { onClick() }) {
                IconButton(onClick = onClick) {
                    Icon(
                        painter = svg,
                        contentDescription = "Comment",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        label = { Text(text = "", color = surfaceColor) }
    )
}