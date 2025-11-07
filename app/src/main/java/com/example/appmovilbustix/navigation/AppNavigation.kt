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
import com.example.appmovilbustix.screens.EventDetailScreen
import com.example.appmovilbustix.screens.EventListScreen
import com.example.appmovilbustix.screens.LoginScreen
import com.example.appmovilbustix.screens.MainScreen
import com.example.appmovilbustix.screens.PlaceholderScreen

// 1. Objeto de rutas definido UNA SOLA VEZ y de forma limpia.
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
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // 2. NavHost único que controla toda la navegación de la app.
    NavHost(navController = navController, startDestination = AppRoutes.LOGIN) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    // Al hacer login, navegamos a la pantalla de eventos por defecto
                    navController.navigate(AppRoutes.EVENTS) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // 3. Función lambda para manejar el click en un evento.
        val onEventClick: (Int) -> Unit = { eventId ->
            navController.navigate("event_detail/$eventId")
        }

        // 4. Definimos aquí todas las pantallas que puede mostrar el Drawer.
        //    Cada una llama a MainScreen, que actúa como un "marco" o "plantilla".
        composable(AppRoutes.EVENTS) {
            MainScreen(navController = navController) { modifier ->
                EventListScreen(
                    events = sampleEvents.filter { it.type == EventType.CONCERT || it.type == EventType.SPORTS },
                    onEventClick = onEventClick,
                    modifier = modifier // Se pasa el modifier con el padding
                )
            }
        }
        composable(AppRoutes.BEACH_TRIPS) {
            MainScreen(navController = navController) { modifier ->
                EventListScreen(
                    events = sampleEvents.filter { it.type == EventType.BEACH },
                    onEventClick = onEventClick,
                    modifier = modifier
                )
            }
        }
        composable(AppRoutes.TOURIST_TRIPS) {
            MainScreen(navController = navController) { modifier ->
                EventListScreen(
                    events = sampleEvents.filter { it.type == EventType.TOURIST },
                    onEventClick = onEventClick,
                    modifier = modifier
                )
            }
        }
        composable(AppRoutes.BUSES) {
            MainScreen(navController = navController) { modifier ->
                BusListScreen(modifier = modifier)
            }
        }
        composable(AppRoutes.ABOUT) {
            MainScreen(navController = navController) { modifier ->
                AboutScreen(modifier = modifier)
            }
        }
        composable(AppRoutes.MY_TICKETS) {
            MainScreen(navController = navController) { modifier ->
                PlaceholderScreen("Aquí verás tus boletos comprados", modifier = modifier)
            }
        }

        // 5. La pantalla de detalle se define fuera del grupo de MainScreen,
        //    porque no necesita el menú lateral.
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
