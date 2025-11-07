package com.example.appmovilbustix.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.data.PurchasedTicket
// ¡CORREGIDO! Apunta a la nueva ubicación en el paquete 'data'
import com.example.appmovilbustix.data.purchasedTicketsList
import com.example.appmovilbustix.utils.QRCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MyTicketsScreen(modifier: Modifier = Modifier) {
    // Ahora usa la lista desde su nueva ubicación correcta
    val tickets = purchasedTicketsList

    if (tickets.isEmpty()) {
        PlaceholderScreen(text = "Aún no has comprado boletos.", modifier = modifier)
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
    // Generamos el QR en un subproceso para no bloquear la UI
    val qrBitmap by produceState<ImageBitmap?>(initialValue = null, ticket.qrCodeContent) {
        value = withContext(Dispatchers.IO) {
            QRCodeGenerator.generateQRCodeBitmap(ticket.qrCodeContent)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(ticket.eventName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Pasajero: ${ticket.passengerName}", style = MaterialTheme.typography.titleMedium)
            Text("Fecha: ${ticket.eventDate}", style = MaterialTheme.typography.bodyMedium)
            Text("ID de Boleto: ${ticket.ticketId}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(16.dp))

            qrBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Código QR para ${ticket.passengerName}",
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Fit
                )
            } ?: run {
                // Muestra un loader mientras se genera el QR
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
