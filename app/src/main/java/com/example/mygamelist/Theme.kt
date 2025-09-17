package com.example.mygamelist

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Black = Color(0xFF000000)
val DarkGray = Color(0xFF1A1A1A)
val Yellow = Color(0xFFFFD600)
val GoldenYellow = Color(0xFFFFA000)
val LightYellow = Color(0xFFFFF8E1)
val NeonYellow = Color(0xFFFFFF00)

val InProgressBlue = Color(0xFFE3F2FD)
val InProgressBlueBorder = Color(0xFF2196F3)
val CompletedGreen = Color(0xFFE8F5E8)
val CompletedGreenBorder = Color(0xFF4CAF50)


val yellowGradient = Brush.horizontalGradient(
    colors = listOf(GoldenYellow, Yellow, NeonYellow)
)

val blackGradient = Brush.verticalGradient(
    colors = listOf(Black, DarkGray)
)

@Composable
fun MyGameListTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Yellow,
            onPrimary = Black,
            background = Color.White,
            onBackground = Black,
            surface = LightYellow,
            onSurface = Black,
            secondary = GoldenYellow,
            onSecondary = Black
        ),
        typography = Typography(

            headlineLarge = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                letterSpacing = 2.sp
            ),
            headlineMedium = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 1.5.sp
            ),
            titleLarge = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.sp
            ),
            bodyLarge = androidx.compose.ui.text.TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        ),
        content = content
    )
}