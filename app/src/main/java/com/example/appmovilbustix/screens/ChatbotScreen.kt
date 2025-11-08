package com.example.appmovilbustix.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.chatbot.ChatbotBrain
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isFromUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(modifier: Modifier = Modifier) {
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var userInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val chatbotBrain = remember { ChatbotBrain() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        if (messages.isEmpty()) {
            messages.add(
                ChatMessage(
                    "¡Hola! Soy BusTix AI. Para comenzar, ¿a qué destino te gustaría cotizar un viaje?",
                    isFromUser = false
                )
            )
        }
    }

    LaunchedEffect(messages.size, isLoading) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            UserInputBar(
                userInput = userInput,
                onUserInputChange = { userInput = it },
                onSendClick = {
                    if (userInput.isNotBlank() && !isLoading) {
                        val userMessage = ChatMessage(userInput, isFromUser = true)
                        messages.add(userMessage)
                        val currentUserInput = userInput
                        userInput = ""
                        isLoading = true

                        coroutineScope.launch {
                            val response = chatbotBrain.sendMessage(currentUserInput)
                            messages.add(ChatMessage(response, isFromUser = false))
                            isLoading = false
                        }
                    }
                },
                isLoading = isLoading
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(message = message)
            }
            if (isLoading) {
                item {
                    TypingIndicator()
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isFromUser) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = "BusTix AI Avatar",
                modifier = Modifier.size(28.dp).padding(end = 8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Card(
            shape = if (message.isFromUser) {
                RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
            } else {
                RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
            },
            colors = CardDefaults.cardColors(
                containerColor = if (message.isFromUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            MarkdownText(
                markdown = message.text,
                modifier = Modifier.padding(12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun UserInputBar(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onSendClick: () -> Unit,
    isLoading: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = onUserInputChange,
                placeholder = { Text("Escribe aquí...") },
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                enabled = userInput.isNotBlank() && !isLoading,
                colors = IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Enviar mensaje")
            }
        }
    }
}

@Composable
fun TypingIndicator() {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            imageVector = Icons.Outlined.AutoAwesome,
            contentDescription = "BusTix AI Avatar",
            modifier = Modifier.size(28.dp).padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Card(
            shape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Escribiendo", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(8.dp))
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
            }
        }
    }
}
