package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.data.*

// Los modelos y datos de ejemplo ahora están en sus propios archivos.

@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Alertas", "Promociones")

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> AlertsList()
            1 -> PromotionsList()
        }
    }
}

@Composable
fun AlertsList() {
    val userAlerts = getSampleUserAlerts()
    if (userAlerts.isEmpty()) {
        PlaceholderScreen(text = "No tienes alertas importantes.")
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(userAlerts) { alert ->
            AlertCard(notification = alert)
        }
    }
}

@Composable
fun PromotionsList() {
    val promotions = samplePromotions
    if (promotions.isEmpty()) {
        PlaceholderScreen(text = "No hay promociones disponibles.")
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(promotions) { promo ->
            PromotionCard(promotion = promo)
        }
    }
}


@Composable
fun AlertCard(notification: UserNotification) {
    val icon: ImageVector
    val cardColor: Color
    val iconColor: Color

    when (notification.type) {
        AlertType.REMINDER -> {
            icon = Icons.Outlined.Schedule
            cardColor = MaterialTheme.colorScheme.surfaceVariant
            iconColor = MaterialTheme.colorScheme.primary
        }
        AlertType.INFO -> {
            icon = Icons.Outlined.Info
            cardColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
            iconColor = MaterialTheme.colorScheme.secondary
        }
        AlertType.WARNING -> {
            icon = Icons.Outlined.WarningAmber
            cardColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            iconColor = MaterialTheme.colorScheme.tertiary
        }
        AlertType.CRITICAL -> {
            icon = Icons.Outlined.Error
            cardColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            iconColor = MaterialTheme.colorScheme.error
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, null, modifier = Modifier.size(32.dp).padding(top = 4.dp), tint = iconColor)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(notification.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(notification.message, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(notification.eventName, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Light)
            }
            Text(notification.timestamp, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp))
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
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, null, Modifier.size(36.dp).padding(top = 4.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(promotion.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(promotion.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
            }
            Text(promotion.timestamp, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.padding(start = 8.dp))
        }
    }
}
