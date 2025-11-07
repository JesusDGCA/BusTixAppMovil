// En: screens/BusScreen.kt

package com.example.appmovilbustix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // Asegúrate de que este import esté
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.data.Bus
import com.example.appmovilbustix.data.sampleBuses

// 1. Añade el parámetro 'modifier: Modifier = Modifier' a la función
@Composable
fun BusListScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        // 2. Usa ese modifier en el LazyColumn
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(sampleBuses) { bus ->
            BusCard(bus = bus)
        }
    }
}

// El resto del archivo (BusCard) no necesita cambios.
@Composable
fun BusCard(bus: Bus) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Image(painterResource(id = bus.imageRes), null, modifier = Modifier.fillMaxWidth().height(180.dp), contentScale = ContentScale.Crop)
            Column(Modifier.padding(16.dp)) {
                Text(bus.name, style = MaterialTheme.typography.titleLarge)
                Text("Capacidad: ${bus.capacity} pasajeros", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                bus.features.forEach { feature ->
                    Text("• $feature", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
