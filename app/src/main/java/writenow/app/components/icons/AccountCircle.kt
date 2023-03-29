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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun BaseAccountCircle(size: Dp) = Icon(
    Icons.Outlined.Person,
    contentDescription = "Test",
    modifier = Modifier.size(size),
    tint = MaterialTheme.colorScheme.onSurface
)

// Default account circle
@Composable
fun AccountCircle(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    onClick: (() -> Unit)? = null
) = BaseCircle(
    modifier = modifier,
    border = Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
    size = size,
    icon = { BaseAccountCircle(size) },
    onClick = onClick
)

// Account circle that uses bitmaps
@Composable
fun AccountSquare(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    size: Dp,
    onClick: (() -> Unit)? = null
) = BaseCircle(
    modifier = modifier,
    border = Modifier.border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
    size = size,
    icon = {bitmap?.let {  btm ->
        Image(bitmap = btm.asImageBitmap(),
            contentDescription =null,
            modifier = Modifier.size(size))
    }},
    onClick = onClick
)
