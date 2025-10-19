package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(onLogin: () -> Unit, onRegister: () -> Unit) {
    Box(Modifier.fillMaxSize().padding(20.dp)) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido a EDDASTORE!", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text("Tienda virtual de calzado", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(32.dp))
            Button(onClick = onLogin, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Login, null); Spacer(Modifier.width(8.dp)); Text("Iniciar sesión")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.PersonAdd, null); Spacer(Modifier.width(8.dp)); Text("Crear cuenta")
            }
        }
    }
}
