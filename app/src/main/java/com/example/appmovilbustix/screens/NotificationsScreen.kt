package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Modelo de datos simple para una promoción
data class Promotion(val title: String, val description: String)

// Datos de ejemplo para las notificaciones
val samplePromotions = listOf(
    Promotion(
        "¡2x1 en Viajes a la Playa!",
        "Compra un boleto para Puerto Vallarta este fin de semana y llévate el segundo a mitad de precio. Válido hasta el domingo."
    ),
    Promotion(
        "15% de Descuento en Conciertos",
        "Usa el código ROCKON25 al comprar tus boletos para el Corona Capital y obtén un 15% de descuento en el transporte."
    ),
    Promotion(
        "Nueva Ruta: San Miguel de Allende",
        "¡Ya disponible! Viaja con nosotros a la ciudad más bonita del mundo. Salidas todos los viernes."
    )
)

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Promociones y Avisos",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(samplePromotions) { promo ->
            PromotionCard(promotion = promo)
        }
    }
}

@Composable
fun PromotionCard(promotion: Promotion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        ListItem(
            headlineContent = { Text(promotion.title, fontWeight = FontWeight.Bold) },
            supportingContent = { Text(promotion.description) },
            leadingContent = {
                Icon(
                    Icons.Default.Campaign,
                    contentDescription = "Promoción",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}