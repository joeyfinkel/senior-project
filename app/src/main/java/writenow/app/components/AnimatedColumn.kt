package writenow.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.PersianOrange

@Composable
fun AnimatedColumn(
    horizontalAlignment: Alignment.Horizontal,
    content: @Composable (ColumnScope.() -> Unit)
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible, enter = fadeIn(
            animationSpec = tween(durationMillis = 500, delayMillis = 200)
        )
    ) {
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(575.dp)
                .shadow(1.dp, RoundedCornerShape(16.dp))
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, PersianOrange, RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = horizontalAlignment,
                    verticalArrangement = Arrangement.Center
                ) {
                    content()
                }
            }
        }
    }
}