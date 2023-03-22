package writenow.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = PersianOrange,
    onPrimary = Color(0xFFeeeeee),
    background = Color(0xFF2c2c2e),
    onBackground = Color(0xFFeeeeee),
    surface = Color(0xFF3a3a3c),
    onSurface = Color(0xFFeeeeee),
    surfaceVariant = Color(0xFFeeeeee),
    onSurfaceVariant = Color(0xFF2c2c2e),
)

private val LightColorScheme = lightColorScheme(
    primary = PersianOrange,
    onPrimary = Color(0xFF2c2c2e),
    background = Background,
    onBackground = Color(0xFF2c2c2e),
    surface = Color(0xFFeeeeee),
    onSurface = Color(0xFF2c2c2e),
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
//    if (!view.isInEditMode) {
//        SideEffect {
//            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
//            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
//        }
//    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}