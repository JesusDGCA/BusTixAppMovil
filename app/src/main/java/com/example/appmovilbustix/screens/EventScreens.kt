// En: screens/EventScreens.kt

package com.example.appmovilbustix.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmovilbustix.data.Event
import com.example.appmovilbustix.data.sampleEvents

@Composable
fun EventListScreen(
    events: List<Event>,
    onEventClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        PlaceholderScreen(
            text = "No hay eventos disponibles en esta categoría.",
            modifier = modifier
        )
        return
    }
    // **CAMBIO IMPORTANTE**: Usamos el modifier que se nos pasa.
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(events) { event ->
            EventCard(event = event, onClick = { onEventClick(event.id) })
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = event.imageRes),
                contentDescription = event.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = event.location, style = MaterialTheme.typography.bodyMedium)
                Text(text = event.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

// =========================================================================================
// El resto del archivo (EventDetailScreen y TicketPurchaseModal) no debería necesitar cambios,
// pero aquí lo incluyo por completitud para que simplemente reemplaces todo el archivo.
// =========================================================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(eventId: Int?, onNavigateBack: () -> Unit) {
    val event = remember(eventId) { sampleEvents.find { it.id == eventId } }
    var showPurchaseModal by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event?.name ?: "Detalle del Evento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Volver") }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Comprar Boletos") },
                icon = { Icon(Icons.Default.ShoppingCart, null) },
                onClick = { showPurchaseModal = true },
                containerColor = MaterialTheme.colorScheme.secondary
            )
        }
    ) { padding ->
        if (event != null) {
            Column(modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = event.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.height(16.dp))
                Text(event.name, style = MaterialTheme.typography.headlineLarge)
                Text("${event.date} - ${event.location}", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                Text("Acerca del Evento", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text(event.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(16.dp))
                Text("Itinerario", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                event.itinerary.forEach { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircleOutline, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(item, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(Modifier.height(100.dp)) // Espacio para el FAB
            }
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Evento no encontrado") }
        }
    }

    if (showPurchaseModal && event != null) {
        TicketPurchaseModal(event = event, onDismiss = { showPurchaseModal = false })
    }
}

@Composable
fun TicketPurchaseModal(event: Event, onDismiss: () -> Unit) {
    var ticketCount by remember { mutableStateOf(1) }
    // Usar SnapshotStateList para que Compose reaccione a los cambios en la lista
    val passengerNames = remember { mutableStateListOf("") }
    val context = LocalContext.current

    // Sincroniza la lista de nombres con la cantidad de boletos
    LaunchedEffect(ticketCount) {
        while (passengerNames.size < ticketCount) passengerNames.add("")
        while (passengerNames.size > ticketCount) passengerNames.removeLast()
    }

    val isFormValid = passengerNames.all { it.isNotBlank() } && ticketCount > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Reservar Asientos", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Asientos:", fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (ticketCount > 1) ticketCount-- }) {
                            Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Quitar boleto")
                        }
                        Text(text = "$ticketCount", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(onClick = { ticketCount++ }) {
                            Icon(Icons.Default.AddCircleOutline, contentDescription = "Añadir boleto")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                // Campos de nombre dinámicos
                passengerNames.forEachIndexed { index, _ ->
                    OutlinedTextField(
                        value = passengerNames[index],
                        onValueChange = { passengerNames[index] = it },
                        label = { Text("Nombre Pasajero ${index + 1}") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        singleLine = true
                    )
                }
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Total: $${ticketCount * event.price}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "¡Reserva para ${passengerNames.first()} y acompañantes realizada!", Toast.LENGTH_LONG).show()
                    onDismiss()
                },
                enabled = isFormValid
            ) { Text("Confirmar Compra") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
