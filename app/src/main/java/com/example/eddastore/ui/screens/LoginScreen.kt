package com.example.eddastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eddastore.ui.auth.AuthViewModel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment

@Composable
fun LoginScreen(
    onLoginOk: () -> Unit,
    onGoRegister: (prefillEmail: String) -> Unit,
    onBack: () -> Unit = {}
) {
    val vm: AuthViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text(text = "Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = pass,
            onValueChange = { pass = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPass = !showPass }) {
                    Icon(
                        imageVector = if (showPass) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPass) "Ocultar" else "Mostrar"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = {
                vm.tryLogin(
                    email = email.trim(),
                    pass = pass.trim(),
                    onOk = onLoginOk,
                    onNeedRegister = { prefill ->
                        // muestra aviso y navega al registro con el email prellenado
                        error = "No existe una cuenta con ese correo."
                        onGoRegister(prefill)
                    },
                    onError = { msg -> error = msg ?: "Credenciales inválidas" }
                )
            }) { Text("Ingresar") }

            Spacer(Modifier.width(12.dp))

            OutlinedButton(onClick = onBack) { Text("Atrás") }
        }

        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Divider()
        Spacer(Modifier.height(12.dp))

        // Botones sociales (solo visuales)
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(
                onClick = { /* TODO: luego OAuth Google */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Continuar con Google",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { /* TODO: luego OAuth Facebook */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Continuar con Facebook",
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        // Botón “Crear cuenta”
        TextButton(onClick = { onGoRegister(email) }) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
