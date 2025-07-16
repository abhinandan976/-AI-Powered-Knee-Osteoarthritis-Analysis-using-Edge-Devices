package com.example.torchapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color

// Define your color scheme
val primaryColor = Color(0xFF6200EE)
val secondaryColor = Color(0xFF03DAC5)

// Light color scheme
val LightColors = lightColorScheme(
    primary = primaryColor,
    secondary = secondaryColor
)

@Composable
fun TorchAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TorchAppTheme {
        // Preview the theme with a simple Text or UI element
    }
}
