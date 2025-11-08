package com.example.appmovilbustix.screens

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appmovilbustix.data.FaqItem
import com.example.appmovilbustix.data.getSampleFaqs

@Composable
fun SupportScreen(modifier: Modifier = Modifier) {
    var showReportDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Centro de Ayuda", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SupportActionCard(
                    icon = Icons.Outlined.HeadsetMic,
                    title = "Contactar a Soporte",
                    subtitle = "Habla con un agente",
                    modifier = Modifier.weight(1f)
                )
                SupportActionCard(
                    icon = Icons.Outlined.ReportProblem,
                    title = "Reportar Problema",
                    subtitle = "Abre un ticket",
                    modifier = Modifier.weight(1f),
                    onClick = { showReportDialog = true }
                )
            }
        }

        item {
            Text(
                "Preguntas Frecuentes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        items(getSampleFaqs()) { faq ->
            FaqCard(faq = faq)
        }
    }

    if (showReportDialog) {
        ReportProblemDialog(onDismiss = { showReportDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupportActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.height(120.dp),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun FaqCard(faq: FaqItem) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().animateContentSize(),
        onClick = { isExpanded = !isExpanded },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(faq.question, modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                Icon(
                    imageVector = if (isExpanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (isExpanded) "Cerrar" else "Expandir"
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(faq.answer, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ReportProblemDialog(onDismiss: () -> Unit) {
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val categories = listOf("Problema con un boleto", "Incidente en el viaje", "Error en la app", "Otro")
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reportar un Problema") },
        text = {
            Column {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selectedCategory ?: "Selecciona una categoría",
                        onValueChange = {}, readOnly = true,
                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = { selectedCategory = category; expanded = false }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Describe el problema") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    Toast.makeText(context, "Reporte enviado. Gracias por tu ayuda.", Toast.LENGTH_LONG).show()
                    onDismiss()
                },
                enabled = description.isNotBlank() && selectedCategory != null
            ) {
                Text("Enviar Reporte")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
