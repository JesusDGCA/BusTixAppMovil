package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appmovilbustix.navigation.AppRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    title: String,
    content: @Composable (Modifier) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                navController = navController,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun AppDrawerContent(navController: NavHostController, closeDrawer: () -> Unit) {
    val navigateTo: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
        closeDrawer()
    }

    ModalDrawerSheet {
        Column(Modifier.padding(16.dp)) {
            Text(
                "BusTix",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            // --- Menú de Navegación ---
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Event, null) },
                label = { Text("Eventos") },
                selected = navController.currentDestination?.route == AppRoutes.EVENTS,
                onClick = { navigateTo(AppRoutes.EVENTS) }
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.BeachAccess, null) },
                label = { Text("Viajes a Playas") },
                selected = navController.currentDestination?.route == AppRoutes.BEACH_TRIPS,
                onClick = { navigateTo(AppRoutes.BEACH_TRIPS) }
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Map, null) },
                label = { Text("Lugares Turísticos") },
                selected = navController.currentDestination?.route == AppRoutes.TOURIST_TRIPS,
                onClick = { navigateTo(AppRoutes.TOURIST_TRIPS) }
            )

            // --- AÑADIDO: ENTRADA PARA EL CHATBOT ---
            // Lo he colocado en una sección principal por su importancia.
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Calculate, null) }, // Ícono de calculadora
                label = { Text("Cotizar Viaje (IA)") },
                selected = navController.currentDestination?.route == AppRoutes.CHATBOT,
                onClick = { navigateTo(AppRoutes.CHATBOT) }
            )

            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Campaign, null) },
                label = { Text("Notificaciones") },
                selected = navController.currentDestination?.route == AppRoutes.NOTIFICATIONS,
                onClick = { navigateTo(AppRoutes.NOTIFICATIONS) }
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            NavigationDrawerItem(
                icon = { Icon(Icons.Default.DirectionsBus, null) },
                label = { Text("Nuestras Unidades") },
                selected = navController.currentDestination?.route == AppRoutes.BUSES,
                onClick = { navigateTo(AppRoutes.BUSES) }
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.ConfirmationNumber, null) },
                label = { Text("Mis Boletos") },
                selected = navController.currentDestination?.route == AppRoutes.MY_TICKETS,
                onClick = { navigateTo(AppRoutes.MY_TICKETS) }
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Info, null) },
                label = { Text("Acerca de") },
                selected = navController.currentDestination?.route == AppRoutes.ABOUT,
                onClick = { navigateTo(AppRoutes.ABOUT) }
            )

            // Spacer para empujar el perfil al fondo
            Spacer(Modifier.weight(1f))

            NavigationDrawerItem(
                icon = { Icon(Icons.Default.Person, null) },
                label = { Text("Mi Perfil") },
                selected = navController.currentDestination?.route == AppRoutes.PROFILE,
                onClick = { navigateTo(AppRoutes.PROFILE) }
            )
        }
    }
}
