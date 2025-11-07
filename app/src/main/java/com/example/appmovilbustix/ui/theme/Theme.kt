package com.example.appmovilbustix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Paleta de colores para el tema oscuro-vino.
// Asegúrate de que los colores (WinePrimary, DarkBackground, etc.)
// estén definidos en tu archivo 'Color.kt'.
private val DarkColorScheme = darkColorScheme(
    primary = WinePrimary,
    secondary = WineSecondary,
    tertiary = WineTertiary,
    background = DarkBackground,
    surface = DarkSurface,
    onPrimary = OnDarkPrimary,
    onSecondary = OnDarkSecondary,
    onTertiary = OnDarkSecondary,
    onBackground = OnDarkSecondary,
    onSurface = OnDarkSecondary,
)

@Composable
fun AppMovilBusTixTheme(
    // Ya no necesitamos los parámetros 'darkTheme' o 'dynamicColor'
    // porque forzaremos nuestro tema personalizado.
    content: @Composable () -> Unit
) {
    // Usamos directamente nuestra paleta de colores personalizada.
    // Esto asegura que la app siempre use el tema oscuro con tonos vino.
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
