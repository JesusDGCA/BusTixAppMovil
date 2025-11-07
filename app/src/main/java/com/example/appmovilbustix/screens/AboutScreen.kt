package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Acerca de BusTix", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "BusTix es tu compañero de viaje ideal para no perderte ningún evento. Nos especializamos en organizar transporte redondo a los mejores conciertos, eventos deportivos y destinos turísticos de México. Viaja seguro, cómodo y sin preocupaciones. ¡Tu única tarea es disfrutar!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// En: screens/AboutScreen.kt
// 1. Añade el parámetro 'modifier: Modifier = Modifier' a la función
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        // 2. Usa ese modifier en el Column principal
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Acerca de BusTix", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "BusTix es tu compañero de viaje ideal para no perderte ningún evento. Nos especializamos en organizar transporte redondo a los mejores conciertos, eventos deportivos y destinos turísticos de México. Viaja seguro, cómodo y sin preocupaciones. ¡Tu única tarea es disfrutar!",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}