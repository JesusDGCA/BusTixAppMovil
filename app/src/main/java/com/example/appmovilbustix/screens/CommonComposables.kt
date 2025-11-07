// En: screens/CommonComposables.kt

package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

// 1. Añade el parámetro 'modifier' a la firma de la función.
@Composable
fun PlaceholderScreen(text: String, modifier: Modifier = Modifier) {
    Box(
        // 2. Usa ese modifier en el Box principal.
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Gray
        )
    }
}
