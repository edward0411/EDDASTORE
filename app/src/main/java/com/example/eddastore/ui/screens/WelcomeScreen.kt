package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onLogin: () -> Unit,
    onGoShop: () -> Unit = {}
) {
    Column(Modifier.padding(16.dp)) {
        Text("Bienvenido a EDDASTORE", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onGoShop) { Text("Entrar a la tienda") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onLogin) { Text("Iniciar sesión") }
    }
}
