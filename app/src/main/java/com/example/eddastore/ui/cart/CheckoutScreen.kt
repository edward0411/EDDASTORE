@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.eddastore.ui.cart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckoutScreen(
    onPayCard: () -> Unit,
    onPayNequi: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Pago") }) }
    ) { pad ->
        Column(
            Modifier.padding(pad).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = onPayCard, modifier = Modifier.fillMaxWidth()) {
                Text("Pagar con tarjeta")
            }
            OutlinedButton(onClick = onPayNequi, modifier = Modifier.fillMaxWidth()) {
                Text("Pagar con Nequi")
            }
            TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
                Text("Volver")
            }
        }
    }
}
