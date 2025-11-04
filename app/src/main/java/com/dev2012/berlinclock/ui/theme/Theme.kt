package com.dev2012.berlinclock.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Red,
    secondary = Yellow,
    tertiary = Pink80,
    outline = Color.White,
    inversePrimary = Color.Transparent
)

private val LightColorScheme = lightColorScheme(
    primary = Red,
    secondary = Yellow,
    tertiary = Pink40,
    outline = Color.Black,
    inversePrimary = Color.Transparent
)

@Composable
fun BerlinClockTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}