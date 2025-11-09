package com.example.eddastore.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreTopBar(
    title: String,
    onCart: () -> Unit,
    // si no hay sesión, esto te lleva a Login
    onUser: () -> Unit,
    // llamado al pulsar "Cerrar sesión"
    onLogout: () -> Unit,
    // si hay nombre => hay sesión
    userName: String?,
    showAddProduct: Boolean,
    onAddProduct: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = onCart) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
            }

            // Ícono de usuario: si hay sesión abre menú; si no, va a Login
            IconButton(onClick = {
                if (userName != null) showMenu = true else onUser()
            }) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Usuario")
            }

            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                if (userName != null) {
                    DropdownMenuItem(
                        text = { Text(userName) },
                        onClick = { /* solo info */ },
                        enabled = false
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Cerrar sesión") },
                        onClick = {
                            showMenu = false
                            onLogout()
                        }
                    )
                }
            }

            if (showAddProduct) {
                IconButton(onClick = onAddProduct) {
                    Icon(Icons.Filled.Add, contentDescription = "Nuevo producto")
                }
            }
        }
    )
}
