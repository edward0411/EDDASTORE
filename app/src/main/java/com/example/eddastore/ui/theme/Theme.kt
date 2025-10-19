package com.example.eddastore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Brand = Color(0xFF1D3557)
private val Accent = Color(0xFFE63946)

private val Light = lightColorScheme(primary = Brand, secondary = Accent)
private val Dark = darkColorScheme(primary = Brand, secondary = Accent)

@Composable
fun EDDATheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, content = content)
}
