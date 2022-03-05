package com.touch.survey.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val LightThemeColors = lightColors(
    primary = Red700,
    primaryVariant = Red700,
    onPrimary = Color.White,
    secondary = Color.White,
    onSecondary = Red700,
    background = Color.White,
    onBackground = Red700,
    surface = Color.White,
    onSurface = Red700,
    error = Red800,
    onError = Color.White
)


val DarkThemeColors = darkColors(
    primary = Purple300,
    primaryVariant = Purple600,
    onPrimary = Color.Black,
    secondary = Color.Black,
    onSecondary = Color.White,
    background = Color.Black,
    onBackground = Color.White,
    surface = Color.Black,
    onSurface = Color.White,
    error = Red300,
    onError = Color.Black
)


val Colors.snackbarAction: Color
    @Composable
    get() = if (isLight) Purple300 else Purple700


val Colors.progressIndicatorBackground: Color
    @Composable
    get() = if (isLight) Color.Black.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.24f)


@Composable
fun MySurveyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkThemeColors
    } else {
        LightThemeColors
    }
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}