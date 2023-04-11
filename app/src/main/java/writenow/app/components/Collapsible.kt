package writenow.app.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun Collapsible(show: Boolean, button: @Composable () -> Unit, content: @Composable () -> Unit) {
    button()
    AnimatedVisibility(
        visible = show, enter = expandVertically(
            expandFrom = Alignment.Top, animationSpec = tween(
                durationMillis = 300, easing = FastOutSlowInEasing
            )
        ), exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = 300, easing = LinearEasing)
        )
    ) {
        content()
    }
}