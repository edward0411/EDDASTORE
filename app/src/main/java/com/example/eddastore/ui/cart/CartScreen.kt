@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.eddastore.ui.cart

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.eddastore.R
import com.example.eddastore.data.AuthState
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.ui.util.sourceFromImageName

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val ctx = LocalContext.current
    val vm: CartViewModel = viewModel(factory = CartViewModel.Factory(AppDb.get(ctx)))
    val user = AuthState.currentUser.value

    LaunchedEffect(user?.id) {
        user?.id?.let { vm.initForUser(it) }
    }
    val cartItems by vm.items.collectAsState(initial = emptyList<CartUiItem>())
    val total by vm.total.collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tu carrito") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Total: $${"%,.0f".format(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = { vm.clear() }) { Text("Cancelar carrito") }
                Button(
                    enabled = user != null && cartItems.isNotEmpty(),
                    onClick = onCheckout
                ) { Text("Pagar") }
            }
        }
    ) { pad ->
        if (cartItems.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(pad),
                contentAlignment = Alignment.Center
            ) { Text("Tu carrito está vacío") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cartItems, key = { it.id }) { item ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AndroidView(
                                modifier = Modifier.size(64.dp),
                                factory = { c -> ImageView(c).apply {
                                    scaleType = ImageView.ScaleType.CENTER_CROP
                                }},
                                update = { img ->
                                    Glide.with(img)
                                        .load(sourceFromImageName(img.context, item.imagePath))
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .centerCrop()
                                        .into(img)
                                }
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                                Text("$${"%,.0f".format(item.price)} · x${item.quantity}")
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { vm.decrease(item) }) { Text("-") }
                                OutlinedButton(onClick = { vm.increase(item) }) { Text("+") }
                                IconButton(onClick = { vm.remove(item) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Quitar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
