package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.chatbot.ChatbotBrain
import kotlinx.coroutines.launch

// Modelo para representar un mensaje en el chat
data class ChatMessage(val text: String, val isFromUser: Boolean)

@Composable
fun ChatbotScreen(modifier: Modifier = Modifier) {
    // Estado para la lista de mensajes
    val messages = remember { mutableStateListOf<ChatMessage>() }
    // Estado para el texto que el usuario está escribiendo
    var userInput by remember { mutableStateOf("") }
    // Estado para indicar si la IA está "pensando"
    var isLoading by remember { mutableStateOf(false) }

    // Instancia del cerebro del chatbot
    val chatbotBrain = remember { ChatbotBrain() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Mensaje de bienvenida inicial
    LaunchedEffect(Unit) {
        messages.add(
            ChatMessage(
                "¡Hola! Soy BusTix AI. Para comenzar, ¿a qué destino te gustaría cotizar un viaje?",
                isFromUser = false
            )
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Área de mensajes (ocupa todo el espacio disponible)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
        }

        // Área de entrada de texto
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Escribe tu consulta...") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (userInput.isNotBlank() && !isLoading) {
                        val userMessage = ChatMessage(userInput, isFromUser = true)
                        messages.add(userMessage)
                        val currentUserInput = userInput
                        userInput = "" // Limpiar el campo
                        isLoading = true

                        coroutineScope.launch {
                            val response = chatbotBrain.sendMessage(currentUserInput)
                            messages.add(ChatMessage(response, isFromUser = false))
                            isLoading = false
                            // Hacer scroll automático al último mensaje
                            listState.animateScrollToItem(messages.size - 1)
                        }
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Send, contentDescription = "Enviar mensaje")
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            // Se vuelve a usar el componente Text normal para evitar el error.
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}
