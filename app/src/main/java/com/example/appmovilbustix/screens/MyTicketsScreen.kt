package com.example.appmovilbustix.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmovilbustix.data.PurchasedTicket
import com.example.appmovilbustix.data.TicketStatus
import com.example.appmovilbustix.data.purchasedTicketsList
import com.example.appmovilbustix.utils.QRCodeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MyTicketsScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Activos", "Usados")
    val tickets = purchasedTicketsList

    val filteredTickets = tickets.filter {
        if (selectedTab == 0) it.status == TicketStatus.ACTIVE else it.status == TicketStatus.USED
    }

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

        if (filteredTickets.isEmpty()) {
            val message = if (selectedTab == 0) "No tienes boletos activos." else "No tienes boletos usados."
            PlaceholderScreen(text = message, modifier = Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredTickets) { ticket ->
                    TicketCard(ticket = ticket)
                }
            }
        }
    }
}

@Composable
fun TicketCard(ticket: PurchasedTicket) {
    var isExpanded by remember { mutableStateOf(false) }
    val qrBitmap by produceState<ImageBitmap?>(initialValue = null, ticket.qrCodeContent) {
        value = withContext(Dispatchers.IO) {
            QRCodeGenerator.generateQRCodeBitmap(ticket.qrCodeContent)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                    Text(ticket.eventName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    TicketInfoItem(icon = Icons.Outlined.Person, label = "Pasajero", value = ticket.passengerName)
                    TicketInfoItem(icon = Icons.Outlined.Chair, label = "Asiento", value = ticket.seatNumber.toString())
                    TicketInfoItem(icon = Icons.Outlined.ConfirmationNumber, label = "ID de Boleto", value = ticket.ticketId)
                }

                DottedVerticalDivider()

                Column(
                    modifier = Modifier.padding(16.dp).width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    qrBitmap?.let {
                        Image(it, "Código QR", Modifier.size(100.dp), contentScale = ContentScale.Fit)
                    } ?: CircularProgressIndicator(Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Muestra este QR al abordar", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, lineHeight = 14.sp)
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Text("Detalles del Viaje", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TicketInfoItem(icon = Icons.Outlined.AccessTime, label = "Hora de Salida", value = ticket.departureTime)
                    TicketInfoItem(icon = Icons.Outlined.PersonPin, label = "Conductor", value = ticket.driverName)
                    TicketInfoItem(icon = Icons.Outlined.DirectionsBus, label = "Unidad", value = ticket.busPlate)
                    Spacer(modifier = Modifier.height(16.dp))
                    TicketActions()
                }
            }
        }
    }
}

@Composable
private fun TicketActions() {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { Toast.makeText(context, "Descargando boleto...", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.Download, "Descargar", Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Descargar")
        }
        OutlinedButton(
            onClick = { Toast.makeText(context, "Enviando por correo...", Toast.LENGTH_SHORT).show() },
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Outlined.Email, "Reenviar", Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Reenviar")
        }
    }
}

@Composable
private fun TicketInfoItem(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Icon(icon, label, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.secondary)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DottedVerticalDivider() {
    Canvas(modifier = Modifier.fillMaxHeight().width(2.dp)) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        drawLine(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            start = Offset(center.x, 0f),
            end = Offset(center.x, size.height),
            strokeWidth = 2f,
            pathEffect = pathEffect
        )
    }
}
