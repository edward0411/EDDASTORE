package com.example.eddastore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = NavyBlue,
    onPrimary = White,
)

private val DarkColors = darkColorScheme(
    primary = White,        // botones blancos en dark
    onPrimary = NavyBlueDark
)

@Composable
fun EDDASTORETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}