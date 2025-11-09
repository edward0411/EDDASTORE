@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.eddastore.ui.product

import android.widget.ImageView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.repo.ProductRepository
import com.example.eddastore.domain.model.Product
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.eddastore.R
import com.example.eddastore.ui.util.copyImageToAppStorage
import com.example.eddastore.ui.util.sourceFromImageName
import com.example.eddastore.ui.util.drawableIdFromName


@Composable
fun ProductFormScreen(
    productId: Long? = null,          // si llega null = crear; si llega id = editar (extensible)
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {


    // Estados del form
    var name by rememberSaveable { mutableStateOf("") }
    var desc by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }
    var imageRef by rememberSaveable { mutableStateOf("") }
    var imagePath by rememberSaveable { mutableStateOf("") }  // nombre del drawable sin extensión
    var isActive by rememberSaveable { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    val ctx = LocalContext.current
    val repo = remember { ProductRepository(AppDb.get(ctx).productDao()) }
    val vm: ProductViewModel = viewModel(factory = ProductViewModel.Factory(repo))
    // picker
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imagePath = copyImageToAppStorage(ctx, uri)  // devuelve "file:///.../images/xxx.jpg"
        }
    }

    LaunchedEffect(productId) {
        if (productId != null) {
            val p = repo.get(productId) ?: return@LaunchedEffect
            name = p.name
            desc = p.description
            price = p.price.toString()
            stock = p.stock.toString()
            imagePath = p.imagePath ?: ""
            isActive = p.isActive
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (productId == null) "Nuevo producto" else "Editar producto") }
            )
        }
    ) { pad ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = desc, onValueChange = { desc = it },
                label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = price, onValueChange = { price = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' } },
                label = { Text("Precio") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            OutlinedTextField(
                value = stock, onValueChange = { stock = it.filter { ch -> ch.isDigit() } },
                label = { Text("Stock") }, singleLine = true, modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = {
                    pickImage.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) { Text("Elegir imagen") }

                Spacer(Modifier.width(8.dp))

                // Mostrar la ruta (opcional)
                Text(
                    if (imagePath.isBlank()) "Sin imagen" else "Imagen seleccionada",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (imagePath.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                AndroidView(
                    modifier = Modifier.size(120.dp),
                    factory = { c -> android.widget.ImageView(c).apply {
                        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    }},
                    update = { img ->
                        Glide.with(img)
                            .load(sourceFromImageName(img.context, imagePath)) // <-- resuelve drawable/uri/file
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .centerCrop()
                            .into(img)
                    }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isActive, onCheckedChange = { isActive = it })
                Text("Activo (visible para clientes)")
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(4.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    // Validaciones simples
                    val p = price.replace(",", ".").toDoubleOrNull()
                    val s = stock.toIntOrNull()
                    when {
                        name.isBlank() -> error = "El nombre es obligatorio"
                        p == null      -> error = "Precio inválido"
                        s == null      -> error = "Stock inválido"
                        else -> {
                            error = null
                            val model = Product(
                                id = productId ?: 0L,
                                name = name.trim(),
                                description = desc.trim(),
                                price = price.replace(",", ".").toDoubleOrNull() ?: 0.0,
                                stock = stock.toIntOrNull() ?: 0,
                                imagePath = imagePath.ifBlank { null },   // ← guardamos ruta
                                isActive = isActive
                            )
                            vm.save(model)
                            onSaved()
                        }
                    }
                }) { Text("Guardar") }

                OutlinedButton(onClick = onCancel) { Text("Cancelar") }
            }
        }
    }
}
