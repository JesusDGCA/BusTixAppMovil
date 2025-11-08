package com.example.appmovilbustix.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmovilbustix.data.EventType
import com.example.appmovilbustix.data.sampleEvents
import com.example.appmovilbustix.screens.*

object AppRoutes {
    const val LOGIN = "login"
    const val EVENT_DETAIL = "event_detail/{eventId}"
    const val SEAT_SELECTION = "seat_selection/{eventId}" // <-- Ruta nueva

    // Rutas de contenido
    const val EVENTS = "events"
    const val BEACH_TRIPS = "beach_trips"
    const val TOURIST_TRIPS = "tourist_trips"
    const val MY_TICKETS = "my_tickets"
    const val BUSES = "buses"
    const val ABOUT = "about"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    const val CHATBOT = "chatbot"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(AppRoutes.EVENTS) {
                    popUpTo(AppRoutes.LOGIN) { inclusive = true }
                }
            })
        }

        val onEventClick: (Int) -> Unit = { eventId ->
            navController.navigate("event_detail/$eventId")
        }

        val screensWithTitles = mapOf(
            AppRoutes.EVENTS to "Eventos y Conciertos",
            AppRoutes.BEACH_TRIPS to "Viajes a la Playa",
            AppRoutes.TOURIST_TRIPS to "Lugares Turísticos",
            AppRoutes.BUSES to "Nuestras Unidades",
            AppRoutes.CHATBOT to "Cotizar Viaje (IA)",
            AppRoutes.NOTIFICATIONS to "Notificaciones",
            AppRoutes.PROFILE to "Mi Perfil",
            AppRoutes.MY_TICKETS to "Mis Boletos",
            AppRoutes.ABOUT to "Acerca de"
        )

        screensWithTitles.forEach { (route, title) ->
            composable(route) {
                MainScreen(navController = navController, title = title) { modifier ->
                    when (route) {
                        AppRoutes.EVENTS -> EventListScreen(sampleEvents.filter { it.type == EventType.CONCERT || it.type == EventType.SPORTS }, onEventClick, modifier)
                        AppRoutes.BEACH_TRIPS -> EventListScreen(sampleEvents.filter { it.type == EventType.BEACH }, onEventClick, modifier)
                        AppRoutes.TOURIST_TRIPS -> EventListScreen(sampleEvents.filter { it.type == EventType.TOURIST }, onEventClick, modifier)
                        AppRoutes.BUSES -> BusListScreen(modifier)
                        AppRoutes.ABOUT -> AboutScreen(modifier)
                        AppRoutes.NOTIFICATIONS -> NotificationsScreen(modifier)
                        AppRoutes.PROFILE -> ProfileScreen(modifier)
                        AppRoutes.MY_TICKETS -> MyTicketsScreen(modifier)
                        AppRoutes.CHATBOT -> ChatbotScreen(modifier)
                    }
                }
            }
        }

        composable(
            route = AppRoutes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull()
            EventDetailScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() },
                onSeatSelection = { eventIdToSelect ->
                    navController.navigate("seat_selection/$eventIdToSelect")
                }
            )
        }

        composable(
            route = AppRoutes.SEAT_SELECTION,
            arguments = listOf(navArgument("eventId") { })
        ) { backStackEntry ->
            SeatSelectionScreen(
                onBack = { navController.popBackStack() },
                onConfirm = { selectedSeats ->
                    // Devolvemos los IDs de los asientos a la pantalla anterior
                    navController.previousBackStackEntry?.savedStateHandle?.set("selected_seat_ids", selectedSeats.map { it.id })
                    navController.popBackStack()
                }
            )
        }
    }
}
