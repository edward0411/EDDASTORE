package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Perfil de usuario") }) }) { inner ->
        Column(
            Modifier.padding(inner).padding(20.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Nombre del Cliente", style = MaterialTheme.typography.titleLarge)
            Text("cliente@correo.com", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.weight(1f))
            OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.ExitToApp, null); Spacer(Modifier.width(8.dp)); Text("Cerrar sesión")
            }
        }
    }
}
