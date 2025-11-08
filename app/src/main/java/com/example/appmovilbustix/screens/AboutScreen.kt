package com.example.appmovilbustix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Facebook
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Instagram
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.R

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- Logo y Título ---
        Image(
            painter = painterResource(id = R.drawable.bustix),
            contentDescription = "Logo de BusTix",
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = "BusTix",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))

        // --- Descripción ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Text(
                text = "BusTix es tu compañero de viaje ideal. Organizamos transporte a los mejores conciertos, eventos y destinos turísticos de México. ¡Viaja seguro y cómodo!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- Información Adicional ---
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            InfoRow(icon = Icons.Outlined.Info, label = "Versión", value = "1.0.0")
            InfoRow(icon = Icons.Outlined.Email, label = "Contacto", value = "soporte@bustix.com")
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Redes Sociales ---
        Text(
            text = "Síguenos en nuestras redes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            IconButton(onClick = { /* Abrir Facebook */ }) {
                Icon(Icons.Outlined.Facebook, contentDescription = "Facebook", modifier = Modifier.size(32.dp))
            }
            IconButton(onClick = { /* Abrir Instagram */ }) {
                Icon(Icons.Outlined.Instagram, contentDescription = "Instagram", modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}
