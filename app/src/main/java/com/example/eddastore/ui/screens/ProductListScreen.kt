// com/example/eddastore/ui/screens/ProductListScreen.kt
package com.example.eddastore.ui.screens

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha   // ← IMPORTANTE
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.eddastore.R
import com.example.eddastore.data.AuthState
import com.example.eddastore.ui.components.StoreTopBar
import com.example.eddastore.ui.product.ProductViewModel
import com.example.eddastore.ui.util.drawableIdFromName
import com.example.eddastore.ui.util.sourceFromImageName

@Composable
fun ProductListScreen(
    onEdit: (Long) -> Unit,
    onAddCart: (Long) -> Unit,
    onGoLogin: () -> Unit,
    onGoCart: () -> Unit,
    onGoProfile: () -> Unit,
    onGoNewProduct: () -> Unit
) {
    val ctx = LocalContext.current
    val vm: ProductViewModel = viewModel(factory = ProductViewModel.provideFactory(ctx))

    val user by AuthState.currentUser
    val isAdmin = user?.role == "admin"
    val userName = user?.fullName

    // Observa productos desde el VM
    val products by remember(isAdmin) {
        if (isAdmin) vm.productsAll() else vm.productsActive()
    }.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            StoreTopBar(
                title = "EDDASTORE",
                onCart = onGoCart,
                onUser = { if (userName != null) onGoProfile() else onGoLogin() },
                onLogout = { AuthState.logout() },
                userName = userName,
                showAddProduct = isAdmin,
                onAddProduct = onGoNewProduct
            )
        }
    ) { pad ->
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(pad), contentAlignment = Alignment.Center) {
                Text("La tienda está vacía por ahora 🛒", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(Modifier.padding(pad).padding(12.dp)) {
                items(products) { p ->
                    val cardAlpha = if (isAdmin && !p.isActive) 0.4f else 1f

                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(cardAlpha)
                            .padding(bottom = 8.dp)
                    ) {
                        Row(
                            Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AndroidView(
                                modifier = Modifier.size(72.dp),
                                factory = { c -> ImageView(c).apply { scaleType = ImageView.ScaleType.CENTER_CROP } },
                                update = { img ->
                                    val source = sourceFromImageName(img.context, p.imagePath)
                                    Glide.with(img)
                                        .load(source)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .centerCrop()
                                        .into(img)
                                }
                            )

                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(p.name, style = MaterialTheme.typography.titleMedium)
                                    if (isAdmin && !p.isActive) {
                                        Spacer(Modifier.width(8.dp))
                                        AssistChip(onClick = {}, label = { Text("INACTIVO") })
                                    }
                                }
                                Text("$${"%,.0f".format(p.price)}", style = MaterialTheme.typography.bodyMedium)
                                if (isAdmin) {
                                    Text("Stock: ${p.stock}", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            Spacer(Modifier.width(8.dp))

                            if (isAdmin) {
                                OutlinedButton(onClick = { onEdit(p.id) }) { Text("Editar") }
                                Spacer(Modifier.width(8.dp))
                                val label = if (p.isActive) "Inactivar" else "Activar"
                                Button(
                                    onClick = { if (p.isActive) vm.deactivate(p) else vm.activate(p) }
                                ) { Text(label) }
                            } else {
                                Button(onClick = { onAddCart(p.id) }) { Text("Agregar") }
                            }
                        }
                    }
                }
            }
        }
    }
}
