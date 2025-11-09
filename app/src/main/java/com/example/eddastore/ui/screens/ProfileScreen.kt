package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onBack: () -> Unit = {}
) {
    Column(Modifier.padding(16.dp)) {
        Text("Perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        Text("Aquí iría la info del usuario…")
        Spacer(Modifier.height(16.dp))
        Row {
            Button(onClick = onLogout) { Text("Cerrar sesión") }
            Spacer(Modifier.width(12.dp))
            OutlinedButton(onClick = onBack) { Text("Atrás") }
        }
    }
}
