package com.example.appmovilbustix.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appmovilbustix.R
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        closeDrawer()
    }

    val currentRoute = navController.currentDestination?.route

    ModalDrawerSheet {
        DrawerHeader()
        Spacer(modifier = Modifier.height(12.dp))

        // --- Menú de Navegación ---
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Event, null) },
            label = { Text("Eventos") },
            selected = currentRoute == AppRoutes.EVENTS,
            onClick = { navigateTo(AppRoutes.EVENTS) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.BeachAccess, null) },
            label = { Text("Viajes a Playas") },
            selected = currentRoute == AppRoutes.BEACH_TRIPS,
            onClick = { navigateTo(AppRoutes.BEACH_TRIPS) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Map, null) },
            label = { Text("Lugares Turísticos") },
            selected = currentRoute == AppRoutes.TOURIST_TRIPS,
            onClick = { navigateTo(AppRoutes.TOURIST_TRIPS) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.AutoAwesome, null) },
            label = { Text("Cotizar Viaje (IA)") },
            selected = currentRoute == AppRoutes.CHATBOT,
            onClick = { navigateTo(AppRoutes.CHATBOT) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Notifications, null) },
            label = { Text("Notificaciones") },
            selected = currentRoute == AppRoutes.NOTIFICATIONS,
            onClick = { navigateTo(AppRoutes.NOTIFICATIONS) }
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.DirectionsBus, null) },
            label = { Text("Nuestras Unidades") },
            selected = currentRoute == AppRoutes.BUSES,
            onClick = { navigateTo(AppRoutes.BUSES) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.ConfirmationNumber, null) },
            label = { Text("Mis Boletos") },
            selected = currentRoute == AppRoutes.MY_TICKETS,
            onClick = { navigateTo(AppRoutes.MY_TICKETS) }
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Info, null) },
            label = { Text("Acerca de") },
            selected = currentRoute == AppRoutes.ABOUT,
            onClick = { navigateTo(AppRoutes.ABOUT) }
        )
        // --- AÑADIDO: SOPORTE ---
        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.HelpOutline, null) },
            label = { Text("Soporte") },
            selected = currentRoute == AppRoutes.SUPPORT,
            onClick = { navigateTo(AppRoutes.SUPPORT) }
        )

        Spacer(Modifier.weight(1f))

        NavigationDrawerItem(
            icon = { Icon(Icons.Outlined.Person, null) },
            label = { Text("Mi Perfil") },
            selected = currentRoute == AppRoutes.PROFILE,
            onClick = { navigateTo(AppRoutes.PROFILE) }
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun DrawerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 20.dp, horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bustix),
            contentDescription = "Logo de BusTix",
            modifier = Modifier.size(50.dp)
        )
        Text(
            text = "BusTix",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}
