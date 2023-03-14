package writenow.app.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Background = Color(0xFFE4E4E4)
val PostBG = Color(0xFFD9D9D9)
val PersianOrange = Color(0xFFD78C62)
val PersianOrangeLight = Color(0x86D78C62)
val TextColor = Color(0xFFeeeeee)
val TextColorLight = Color(0xB0EEEEEE)
val TextColorDark = Color(0xFF2c2c2e)

fun PlaceholderColor(darkMode: Boolean) = if (darkMode) TextColorLight else TextColorDark
