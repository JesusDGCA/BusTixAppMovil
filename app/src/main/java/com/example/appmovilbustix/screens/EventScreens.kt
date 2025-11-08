package com.example.appmovilbustix.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmovilbustix.data.Event
import com.example.appmovilbustix.data.PurchasedTicket
import com.example.appmovilbustix.data.sampleEvents
import com.example.appmovilbustix.data.purchasedTicketsList
import java.util.UUID

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
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.height(200.dp)) {
            Image(
                painter = painterResource(id = event.imageRes),
                contentDescription = event.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Disponible",
                        color = Color.White,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
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
fun EventDetailScreen(eventId: Int?, onNavigateBack: () -> Unit) {
    val event = remember(eventId) { sampleEvents.find { it.id == eventId } }
    var showPurchaseModal by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    if (event == null) {
        Scaffold {
            Box(Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                Text("Evento no encontrado")
            }
        }
        return
    }

    val topBarColor = if (scrollState.value > 250)
        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    else
        Color.Transparent

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(visible = scrollState.value > 300) {
                        Text(event.name)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = if (scrollState.value > 300) MaterialTheme.colorScheme.onSurface else Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = topBarColor)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Comprar Boletos") },
                icon = { Icon(Icons.Default.ShoppingCart, null) },
                onClick = { showPurchaseModal = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(scrollState)
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                Image(painterResource(id = event.imageRes), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent), endY = 250f)))
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
        TicketPurchaseModal(event = event, onDismiss = { showPurchaseModal = false })
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
fun TicketPurchaseModal(event: Event, onDismiss: () -> Unit) {
    var ticketCount by remember { mutableStateOf(1) }
    val passengerNames = remember { mutableStateListOf("") }
    val context = LocalContext.current

    LaunchedEffect(ticketCount) {
        while (passengerNames.size < ticketCount) passengerNames.add("")
        while (passengerNames.size > ticketCount) passengerNames.removeLast()
    }
    val isFormValid = passengerNames.all { it.isNotBlank() } && ticketCount > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reservar Asientos", fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Outlined.ConfirmationNumber, null, Modifier.size(28.dp))
                    Spacer(Modifier.width(12.dp))
                    Text("Asientos:", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { if (ticketCount > 1) ticketCount-- }) { Icon(Icons.Default.RemoveCircleOutline, "Quitar") }
                        Text("$ticketCount", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 8.dp))
                        IconButton(onClick = { ticketCount++ }) { Icon(Icons.Default.AddCircleOutline, "Añadir") }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Pasajeros:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                passengerNames.forEachIndexed { index, _ ->
                    OutlinedTextField(
                        value = passengerNames[index],
                        onValueChange = { passengerNames[index] = it },
                        label = { Text("Nombre Pasajero ${index + 1}") },
                        leadingIcon = { Icon(Icons.Outlined.Person, null) },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                passengerNames.forEach { name ->
                    if (name.isNotBlank()) {
                        val ticketId = UUID.randomUUID().toString().substring(0, 8).uppercase()
                        val qrContent = "EVENT:${event.name}|TICKET_ID:$ticketId|PAX:$name"
                        purchasedTicketsList.add(PurchasedTicket(ticketId, event.name, event.date, name, qrContent))
                    }
                }
                Toast.makeText(context, "¡Compra exitosa!", Toast.LENGTH_LONG).show()
                onDismiss()
            }, enabled = isFormValid) { Text("Confirmar Compra") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
