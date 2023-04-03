package writenow.app.components.icons

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun BaseAccountCircle(size: Dp) = Icon(
    Icons.Outlined.Person,
    contentDescription = "Profile picture",
    modifier = Modifier.size(size),
    tint = MaterialTheme.colorScheme.onSurface
)

// Default account circle
@Composable
fun AccountCircle(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    bitmap: Bitmap? = null,
    onClick: (() -> Unit)? = null
) = BaseCircle(
    modifier = modifier,
    border = Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
    size = size,
    icon = {
        if (bitmap == null) {
            BaseAccountCircle(size)
        } else {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(size)
                    .clip(CircleShape),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
            )
        }
    },
    onClick = onClick
)

