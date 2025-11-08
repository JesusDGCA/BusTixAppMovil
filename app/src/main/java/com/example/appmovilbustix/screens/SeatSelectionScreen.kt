package com.example.appmovilbustix.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmovilbustix.data.Seat
import com.example.appmovilbustix.data.SeatStatus
import com.example.appmovilbustix.data.generateSampleSeats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(onBack: () -> Unit, onConfirm: (List<Seat>) -> Unit) {
    var seats by remember { mutableStateOf(generateSampleSeats()) }
    val selectedSeats = seats.filter { it.status == SeatStatus.SELECTED }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecciona tus Asientos") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        },
        bottomBar = {
            SummaryBottomBar(selectedSeats = selectedSeats, onConfirm = { onConfirm(selectedSeats) })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            SeatLegend()
            Spacer(modifier = Modifier.height(24.dp))
            BusLayout(seats = seats, onSeatClick = {
                val clickedSeat = seats.find { it.id == it.id }!!
                if (clickedSeat.status == SeatStatus.AVAILABLE) {
                    seats = seats.map { if (it.id == clickedSeat.id) it.copy(status = SeatStatus.SELECTED) else it }
                } else if (clickedSeat.status == SeatStatus.SELECTED) {
                    seats = seats.map { if (it.id == clickedSeat.id) it.copy(status = SeatStatus.AVAILABLE) else it }
                }
            })
        }
    }
}

@Composable
private fun BusLayout(seats: List<Seat>, onSeatClick: (Seat) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // 4 asientos + 1 pasillo
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(seats) { seat ->
            if (seat.number % 4 == 3) { // Añadir un espacio para el pasillo
                Spacer(modifier = Modifier.size(24.dp))
            }
            SeatIcon(seat = seat, onClick = { onSeatClick(seat) })
        }
    }
}

@Composable
private fun SeatIcon(seat: Seat, onClick: () -> Unit) {
    val color = when (seat.status) {
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.surface
        SeatStatus.SELECTED -> MaterialTheme.colorScheme.primary
        SeatStatus.TAKEN -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when (seat.status) {
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.onSurface
        SeatStatus.SELECTED -> MaterialTheme.colorScheme.onPrimary
        SeatStatus.TAKEN -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Icon(
        imageVector = Icons.Default.Chair,
        contentDescription = "Asiento ${seat.number}",
        modifier = Modifier
            .size(40.dp)
            .clickable(enabled = seat.status != SeatStatus.TAKEN, onClick = onClick)
            .background(color, RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(4.dp),
        tint = contentColor
    )
}

@Composable
private fun SeatLegend() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        LegendItem(color = MaterialTheme.colorScheme.surface, text = "Disponible")
        LegendItem(color = MaterialTheme.colorScheme.primary, text = "Seleccionado")
        LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, text = "Ocupado")
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(16.dp).background(color).border(1.dp, Color.Black))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun SummaryBottomBar(selectedSeats: List<Seat>, onConfirm: () -> Unit) {
    val totalPrice = selectedSeats.sumOf { it.price }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Asientos: ${selectedSeats.joinToString { it.number.toString() }}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total: $${String.format("%.2f", totalPrice)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = selectedSeats.isNotEmpty()
            ) {
                Text("Confirmar Selección", fontSize = 16.sp)
            }
        }
    }
}
