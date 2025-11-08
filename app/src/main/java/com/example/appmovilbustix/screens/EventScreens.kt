package com.example.appmovilbustix.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.appmovilbustix.data.*
import com.example.appmovilbustix.navigation.AppRoutes
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.util.UUID
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

// ViewModel para compartir estado
class EventViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    var selectedSeats by mutableStateOf<List<Seat>>(emptyList())
}

@Composable
fun EventListScreen(
    events: List<Event>,
    onEventClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (events.isEmpty()) {
        PlaceholderScreen(text = "No hay eventos disponibles.", modifier = modifier)
        return
    }
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
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(painterResource(id = event.imageRes), event.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 300f)))
            Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text("Disponible", color = Color.White, modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                }
                Column {
                    Text(event.name, style = MaterialTheme.typography.titleLarge, color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${event.date} - ${event.location}", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int?,
    onNavigateBack: () -> Unit,
    onSeatSelection: (Int) -> Unit,
    navController: NavController = rememberNavController()
) {
    val event = remember(eventId) { sampleEvents.find { it.id == eventId } }
    var showPurchaseModal by remember { mutableStateOf(false) }
    var selectedSeatIds by remember { mutableStateOf<List<String>>(emptyList()) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = navController.currentBackStackEntry?.savedStateHandle?.getLiveData<List<String>>("selected_seat_ids")?.observe(lifecycleOwner) {
            if (it.isNotEmpty()) {
                selectedSeatIds = it
                showPurchaseModal = true
                // Limpiar el estado para no volver a activarlo
                navController.currentBackStackEntry?.savedStateHandle?.remove<List<String>>("selected_seat_ids")
            }
        }
        onDispose { observer?.let { navController.currentBackStackEntry?.savedStateHandle?.getLiveData<List<String>>("selected_seat_ids")?.removeObserver(it) } }
    }

    if (event == null) {
        Scaffold { Box(Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) { Text("Evento no encontrado") } }
        return
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Comprar Boletos") },
                icon = { Icon(Icons.Default.ShoppingCart, null) },
                onClick = { onSeatSelection(event.id) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding()).verticalScroll(scrollState)) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(painterResource(id = event.imageRes), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(event.name, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                InfoRow(icon = Icons.Outlined.CalendarToday, text = event.date)
                InfoRow(icon = Icons.Outlined.LocationOn, text = event.location)
                Divider(modifier = Modifier.padding(vertical = 20.dp))
                Text("Acerca del Evento", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(event.description, style = MaterialTheme.typography.bodyLarge, lineHeight = 24.sp)
                Spacer(Modifier.height(100.dp))
            }
        }
    }

    if (showPurchaseModal) {
        val seatsToPurchase = remember(selectedSeatIds) { generateSampleSeats().filter { it.id in selectedSeatIds } }
        TicketPurchaseModal(
            event = event,
            selectedSeats = seatsToPurchase,
            onDismiss = { showPurchaseModal = false }
        )
    }
}


@Composable
private fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TicketPurchaseModal(event: Event, selectedSeats: List<Seat>, onDismiss: () -> Unit) {
    var passengerNames by remember { mutableStateOf(List(selectedSeats.size) { "" }) }
    val context = LocalContext.current
    val isFormValid = passengerNames.all { it.isNotBlank() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmar Compra", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text("Asientos seleccionados: ${selectedSeats.joinToString { it.number.toString() }}")
                Spacer(Modifier.height(16.dp))
                Text("Ingresa los nombres de los pasajeros:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                selectedSeats.forEachIndexed { index, seat ->
                    OutlinedTextField(
                        value = passengerNames[index],
                        onValueChange = { newName ->
                            passengerNames = passengerNames.toMutableList().also { it[index] = newName }
                        },
                        label = { Text("Pasajero para asiento ${seat.number}") },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                selectedSeats.forEachIndexed { index, seat ->
                    val name = passengerNames[index]
                    val ticketId = UUID.randomUUID().toString().substring(0, 8).uppercase()
                    val qrContent = "EVENT:${event.name}|SEAT:${seat.number}|TICKET_ID:$ticketId|PAX:$name"
                    purchasedTicketsList.add(PurchasedTicket(ticketId, event.name, event.date, name, qrContent))
                }
                Toast.makeText(context, "¡Compra exitosa! Revisa tus boletos.", Toast.LENGTH_LONG).show()
                onDismiss()
            }, enabled = isFormValid) { Text("Confirmar y Pagar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
