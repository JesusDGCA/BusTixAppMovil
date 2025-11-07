package com.example.appmovilbustix.data

import com.example.appmovilbustix.R

// --- DATOS DE EJEMPLO AMPLIADOS ---

val sampleEvents = listOf(
    // Conciertos
    Event(1, "Corona Capital", "15 NOV 2025", "CDMX", R.drawable.corona, EventType.CONCERT,
        "El festival de música internacional más grande de México. Disfruta de bandas de rock, pop e indie de todo el mundo.",
        listOf("Salida: 6:00 AM", "Llegada al evento: 1:00 PM", "Regreso: Al finalizar el evento", "Punto de encuentro: Auditorio Nacional"),
        1500),
    Event(2, "Flow Fest", "25 NOV 2025", "Foro Sol, CDMX", R.drawable.flowfest, EventType.CONCERT,
        "El festival de reggaetón y música urbana más esperado. Baila con los mejores artistas del género.",
        listOf("Salida: 8:00 AM", "Llegada al evento: 2:00 PM", "Regreso: 2:00 AM del día siguiente", "Punto de encuentro: World Trade Center"),
        1350),

    // Deportes
    Event(3, "Final Liga MX", "22 DIC 2025", "Estadio Azteca", R.drawable.ligamx, EventType.SPORTS,
        "Vive la emoción del partido final del torneo de fútbol mexicano en el icónico Estadio Azteca.",
        listOf("Salida: 4:00 PM", "Llegada al estadio: 6:00 PM", "Regreso: 11:00 PM", "Punto de encuentro: Ángel de la Independencia"),
        950),

    // Playas
    Event(4, "Fin de Año en Cancún", "29 DIC 2025 - 2 ENE 2026", "Cancún, Q.R.", R.drawable.cancun, EventType.BEACH,
        "Recibe el año nuevo en las paradisíacas playas de Cancún. Incluye transporte y hospedaje (opcional).",
        listOf("Día 1: Viaje a Cancún", "Día 2: Día de playa y actividades acuáticas", "Día 3: Fiesta de Fin de Año", "Día 4: Regreso"),
        4500),
    Event(5, "Escapada a Puerto Vallarta", "15 MAR 2026", "Puerto Vallarta, JAL", R.drawable.puerto, EventType.BEACH,
        "Disfruta de un fin de semana en uno de los destinos de playa más vibrantes de México.",
        listOf("Salida: Viernes 10:00 PM", "Llegada: Sábado 7:00 AM", "Regreso: Domingo 5:00 PM", "Punto de encuentro: Central de Autobuses"),
        2200),


    // Turismo
    Event(6, "Ruta del Vino", "18 FEB 2026", "Valle de Guadalupe", R.drawable.ruta, EventType.TOURIST,
        "Un recorrido por las mejores casas vinícolas de Baja California. Incluye degustaciones y comida campestre.",
        listOf("Salida: 9:00 AM", "Visita a 3 viñedos", "Comida incluida", "Regreso: 8:00 PM"),
        1800)
)

val sampleBuses = listOf(
    Bus(1, "Irizar i8", 48, listOf("Asientos reclinables", "Aire acondicionado", "Baño", "Wi-Fi"), R.drawable.irizar),
    Bus(2, "Volvo 9800", 50, listOf("Doble piso", "Pantallas individuales", "Baño XL", "Conectores USB"), R.drawable.volvo),
    Bus(3, "Sprinter de Lujo", 18, listOf("Asientos de piel", "Minibar", "Atención personalizada", "Ideal para grupos pequeños"), R.drawable.sprinter)
)

