package com.example.appmovilbustix.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.data.PurchasedTicket
import com.example.appmovilbustix.data.purchasedTicketsList
import com.example.appmovilbustix.utils.QRCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MyTicketsScreen(modifier: Modifier = Modifier) {
    val tickets = purchasedTicketsList

    if (tickets.isEmpty()) {
        PlaceholderScreen(
            text = "Aún no has comprado boletos. Tus boletos aparecerán aquí cuando completes una compra.",
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tickets) { ticket ->
                TicketCard(ticket = ticket)
            }
        }
    }
}

@Composable
fun TicketCard(ticket: PurchasedTicket) {
    val qrBitmap by produceState<ImageBitmap?>(initialValue = null, ticket.qrCodeContent) {
        value = withContext(Dispatchers.IO) {
            QRCodeGenerator.generateQRCodeBitmap(ticket.qrCodeContent)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            // --- Columna de Información ---
            Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                Text(ticket.eventName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                TicketInfoItem(icon = Icons.Outlined.Person, label = "Pasajero", value = ticket.passengerName)
                TicketInfoItem(icon = Icons.Outlined.CalendarToday, label = "Fecha", value = ticket.eventDate)
                TicketInfoItem(icon = Icons.Outlined.ConfirmationNumber, label = "ID de Boleto", value = ticket.ticketId)
            }

            // --- Separador Punteado ---
            DottedVerticalDivider()

            // --- Columna del QR ---
            Column(
                modifier = Modifier.padding(16.dp).width(120.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                qrBitmap?.let {
                    Image(
                        bitmap = it,
                        contentDescription = "Código QR para ${ticket.passengerName}",
                        modifier = Modifier.size(100.dp),
                        contentScale = ContentScale.Fit
                    )
                } ?: CircularProgressIndicator(modifier = Modifier.size(40.dp))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Muestra este QR al abordar",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TicketInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DottedVerticalDivider() {
    Canvas(modifier = Modifier.fillMaxHeight().width(2.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        drawLine(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            start = Offset(x = center.x, y = 0f),
            end = Offset(x = center.x, y = size.height),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}
