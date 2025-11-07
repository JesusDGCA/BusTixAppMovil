package com.example.appmovilbustix.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appmovilbustix.data.EventType
import com.example.appmovilbustix.data.sampleEvents
import com.example.appmovilbustix.screens.AboutScreen
import com.example.appmovilbustix.screens.BusListScreen
// 1. IMPORTA LA NUEVA PANTALLA DEL CHATBOT
import com.example.appmovilbustix.screens.ChatbotScreen
import com.example.appmovilbustix.screens.EventDetailScreen
import com.example.appmovilbustix.screens.EventListScreen
import com.example.appmovilbustix.screens.LoginScreen
import com.example.appmovilbustix.screens.MainScreen
import com.example.appmovilbustix.screens.MyTicketsScreen
import com.example.appmovilbustix.screens.NotificationsScreen
import com.example.appmovilbustix.screens.ProfileScreen


// 2. AÑADE LA RUTA PARA EL CHATBOT
object AppRoutes {
    const val LOGIN = "login"
    const val EVENT_DETAIL = "event_detail/{eventId}"

    // Rutas de contenido que se mostrarán dentro de MainScreen
    const val EVENTS = "events"
    const val BEACH_TRIPS = "beach_trips"
    const val TOURIST_TRIPS = "tourist_trips"
    const val MY_TICKETS = "my_tickets"
    const val BUSES = "buses"
    const val ABOUT = "about"
    const val NOTIFICATIONS = "notifications"
    const val PROFILE = "profile"
    const val CHATBOT = "chatbot" // <-- Ruta nueva
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.EVENTS) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        val onEventClick: (Int) -> Unit = { eventId ->
            navController.navigate("event_detail/$eventId")
        }

        // 3. AÑADE EL CHATBOT AL MAPA DE TÍTULOS
        val screensWithTitles = mapOf(
            AppRoutes.EVENTS to "Eventos y Conciertos",
            AppRoutes.BEACH_TRIPS to "Viajes a la Playa",
            AppRoutes.TOURIST_TRIPS to "Lugares Turísticos",
            AppRoutes.BUSES to "Nuestras Unidades",
            AppRoutes.CHATBOT to "Cotizar Viaje (IA)", // <-- Título para la TopAppBar
            AppRoutes.NOTIFICATIONS to "Notificaciones",
            AppRoutes.PROFILE to "Mi Perfil",
            AppRoutes.MY_TICKETS to "Mis Boletos",
            AppRoutes.ABOUT to "Acerca de"
        )

        screensWithTitles.forEach { (route, title) ->
            composable(route) {
                MainScreen(
                    navController = navController,
                    title = title
                ) { modifier ->
                    when (route) {
                        AppRoutes.EVENTS -> EventListScreen(
                            events = sampleEvents.filter { it.type == EventType.CONCERT || it.type == EventType.SPORTS },
                            onEventClick = onEventClick,
                            modifier = modifier
                        )
                        AppRoutes.BEACH_TRIPS -> EventListScreen(
                            events = sampleEvents.filter { it.type == EventType.BEACH },
                            onEventClick = onEventClick,
                            modifier = modifier
                        )
                        AppRoutes.TOURIST_TRIPS -> EventListScreen(
                            events = sampleEvents.filter { it.type == EventType.TOURIST },
                            onEventClick = onEventClick,
                            modifier = modifier
                        )
                        AppRoutes.BUSES -> BusListScreen(modifier = modifier)
                        AppRoutes.ABOUT -> AboutScreen(modifier = modifier)
                        AppRoutes.NOTIFICATIONS -> NotificationsScreen(modifier = modifier)
                        AppRoutes.PROFILE -> ProfileScreen(modifier = modifier)
                        AppRoutes.MY_TICKETS -> MyTicketsScreen(modifier = modifier)

                        // 4. AÑADE LA PANTALLA DEL CHATBOT AL 'when'
                        AppRoutes.CHATBOT -> ChatbotScreen(modifier = modifier)
                    }
                }
            }
        }

        // La pantalla de detalle se mantiene separada
        composable(
            route = AppRoutes.EVENT_DETAIL,
            arguments = listOf(navArgument("eventId") { })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull()
            EventDetailScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
