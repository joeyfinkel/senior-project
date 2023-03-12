package writenow.app.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import writenow.app.ui.theme.DefaultWidth
import writenow.app.ui.theme.PostBG
import writenow.app.ui.theme.Primary

@Composable
fun PostContainer(
    modifier: Modifier = Modifier,
    fullHeight: Boolean = false,
    halfHeight: Boolean = false,
    width: Dp = DefaultWidth + 100.dp,
    height: Dp? = null,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val heights = if (fullHeight) {
        Modifier.fillMaxHeight()
    } else if (halfHeight) {
        Modifier.fillMaxHeight(0.5f)
    } else {
        Modifier.height(height = height ?: 200.dp)
    }

    Box(
        modifier = Modifier
            .width(width = width)
            .then(heights)
            .then(modifier)
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .border(2.dp, Primary, RoundedCornerShape(16.dp))
                .background(PostBG)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                content()
            }
        }
    }
}