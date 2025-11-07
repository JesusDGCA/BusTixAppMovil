package com.example.appmovilbustix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.appmovilbustix.navigation.AppNavigation
import com.example.appmovilbustix.ui.theme.AppMovilBusTixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMovilBusTixTheme {
                AppNavigation() // Inicia la navegación de la app
            }
        }
    }
}
