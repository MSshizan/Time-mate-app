package com.example.clock.Presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Typography

// Define your light color scheme
private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE), // Primary color
    onPrimary = Color.White,     // Color for text/icons on primary color
    primaryContainer = Color(0xFFBB86FC),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFFBB86FC),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFBB86FC),
    onSecondaryContainer = Color.Black,
    // Add other color properties as needed
)

// Define your light typography
private val Typography = Typography(
    // Define typography settings if needed
)

@Composable
fun ClockThemes(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}