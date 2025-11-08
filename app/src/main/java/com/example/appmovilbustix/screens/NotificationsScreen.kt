package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

enum class NotificationType { DISCOUNT, NEW_ROUTE, OFFER }

data class Promotion(
    val title: String,
    val description: String,
    val type: NotificationType,
    val timestamp: String
)

val samplePromotions = listOf(
    Promotion(
        "¡2x1 en Viajes a la Playa!",
        "Compra un boleto para Puerto Vallarta y llévate el segundo a mitad de precio. Válido hasta el domingo.",
        NotificationType.OFFER,
        "Hace 1h"
    ),
    Promotion(
        "15% de Descuento en Conciertos",
        "Usa el código ROCKON25 al comprar tus boletos para el Corona Capital y obtén un 15% de descuento.",
        NotificationType.DISCOUNT,
        "Hace 5h"
    ),
    Promotion(
        "Nueva Ruta: San Miguel de Allende",
        "¡Ya disponible! Viaja con nosotros a la ciudad más bonita de México. Salidas todos los viernes.",
        NotificationType.NEW_ROUTE,
        "Ayer"
    ),
    Promotion(
        "Fin de semana en Tequila, Jalisco",
        "Descubre el pueblo mágico de Tequila con nuestro tour especial. Incluye degustaciones y transporte.",
        NotificationType.NEW_ROUTE,
        "Hace 2 días"
    )
)

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    if (samplePromotions.isEmpty()) {
        PlaceholderScreen(text = "No tienes notificaciones.", modifier = modifier)
        return
    }
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
    val icon = when (promotion.type) {
        NotificationType.DISCOUNT -> Icons.Outlined.LocalOffer
        NotificationType.NEW_ROUTE -> Icons.Outlined.Explore
        NotificationType.OFFER -> Icons.Outlined.Star
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Notificación",
                modifier = Modifier.size(36.dp).padding(top = 4.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promotion.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = promotion.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
            Text(
                text = promotion.timestamp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}