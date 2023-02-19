package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

val fonts =
    FontFamily(
        Font(R.font.neuton_regular),
        Font(R.font.neuton_bold, weight = FontWeight.Bold),
        Font(R.font.neuton_light, weight = FontWeight.Light),
        Font(R.font.neuton_extra_bold, weight = FontWeight.ExtraBold),
        Font(R.font.neuton_extra_light, weight = FontWeight.ExtraLight),
        Font(R.font.neuton_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    )

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fonts,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    /* Other default text styles to override
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)