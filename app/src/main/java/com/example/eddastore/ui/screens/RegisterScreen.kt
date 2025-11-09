package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eddastore.ui.auth.AuthViewModel

@Composable
fun RegisterScreen(
    prefillEmail: String? = null,
    onRegisterOk: () -> Unit,
    onBack: () -> Unit
) {
    val vm: AuthViewModel = viewModel()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(prefillEmail ?: "") }
    var pass by remember { mutableStateOf("") }
    var pass2 by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Nombre completo") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, singleLine = true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pass2, onValueChange = { pass2 = it }, label = { Text("Confirmar contraseña") }, singleLine = true)

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (fullName.isBlank() || email.isBlank() || pass.isBlank()) {
                msg = "Todos los campos son obligatorios."
                return@Button
            }
            if (pass != pass2) {
                msg = "Las contraseñas no coinciden."
                return@Button
            }
            vm.register(fullName, email, pass,
                onOk = onRegisterOk,
                onError = { msg = it }
            )
        }) { Text("Registrarme") }

        msg?.let { Spacer(Modifier.height(12.dp)); Text(it, color = MaterialTheme.colorScheme.error) }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Atrás") }
    }
}
