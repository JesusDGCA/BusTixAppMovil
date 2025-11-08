package com.example.appmovilbustix.data

// Enum para representar el estado de un asiento
enum class SeatStatus {
    AVAILABLE, // Disponible
    SELECTED,  // Seleccionado por el usuario actual
    TAKEN      // Ocupado por otro usuario
}

// Data class para representar un asiento individual
data class Seat(
    val id: String,          // Identificador único, p.ej. "A1"
    val number: Int,         // Número visible del asiento
    val status: SeatStatus,  // Estado actual del asiento
    val price: Double = 250.00 // Precio del asiento (puede variar)
)

/**
 * Genera una lista de asientos de ejemplo para un autobús.
 * @return Una lista de 40 asientos con algunos ya ocupados.
 */
fun generateSampleSeats(): List<Seat> {
    val seats = mutableListOf<Seat>()
    val rows = 10
    val columns = 4
    val takenSeats = listOf("A3", "B1", "B2", "F4", "H3")

    for (row in 1..rows) {
        for (col in 1..columns) {
            val seatId = "${'A' + row - 1}$col"
            val seatNumber = (row - 1) * columns + col
            val status = if (takenSeats.contains(seatId)) SeatStatus.TAKEN else SeatStatus.AVAILABLE
            seats.add(Seat(id = seatId, number = seatNumber, status = status))
        }
    }
    return seats
}
