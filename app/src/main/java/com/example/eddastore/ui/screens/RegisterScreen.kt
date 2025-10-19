package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterOk: () -> Unit, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Clientes") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner).padding(20.dp)) {
            OutlinedTextField(name, { name = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(email, { email = it }, label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(pass, { pass = it }, label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(confirm, { confirm = it }, label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation())

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onRegisterOk,
                enabled = name.isNotBlank() && email.isNotBlank() && pass.length >= 6 && pass == confirm,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Registrarme") }
        }
    }
}
