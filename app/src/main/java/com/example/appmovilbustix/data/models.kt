package com.example.appmovilbustix.data

data class Event(
    val id: Int,
    val name: String,
    val date: String,
    val location: String,
    val imageRes: Int,
    val type: EventType,
    val description: String,
    val itinerary: List<String>,
    val price: Int
)

enum class EventType { CONCERT, SPORTS, BEACH, TOURIST }

data class Bus(
    val id: Int,
    val name: String,
    val capacity: Int,
    val features: List<String>,
    val imageRes: Int
)
