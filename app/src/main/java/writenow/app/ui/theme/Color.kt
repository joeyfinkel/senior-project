package writenow.app.ui.theme

import androidx.compose.ui.graphics.Color

val Background = Color(0xFFE4E4E4)
val PersianOrange = Color(0xFFD78C62)
val PersianOrangeLight = Color(0x86D78C62)
val TextColorLight = Color(0xB0EEEEEE)
val TextColorDark = Color(0xFF2c2c2e)

fun PlaceholderColor(darkMode: Boolean) = if (darkMode) TextColorLight else TextColorDark
