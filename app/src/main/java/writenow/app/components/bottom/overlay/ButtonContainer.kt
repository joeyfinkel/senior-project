package writenow.app.components.bottom.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomOverlayButtonContainer(
    horizontalPadding: Dp = 15.dp,
    verticalPadding: Dp = 5.dp,
    verticalSpacing: Dp = 10.dp,
    layoutId: String,
    content: @Composable () -> Unit
) =
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding)
            .layoutId(layoutId),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
        horizontalAlignment = Alignment.Start
    ) {
        content()
    }