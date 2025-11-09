package com.example.eddastore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.local.entity.CartEntity
import com.example.eddastore.data.local.entity.CartItemEntity
import com.example.eddastore.domain.model.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CartViewModel(
    private val db: AppDb
) : ViewModel() {

    // carrito activo
    private val cartId = MutableStateFlow<Long?>(null)

    private val _items = MutableStateFlow<List<CartUiItem>>(emptyList())
    val items: StateFlow<List<CartUiItem>> = _items.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    /** Llamar cuando cambie el usuario logueado */
    fun initForUser(userId: Long) = viewModelScope.launch {
        // crea/obtiene carrito activo
        val id = db.cartDao().getOrCreateActiveCart(userId)
        cartId.value = id
        // Observa los items de ese carrito
        db.cartDao().observeItems(id).collect { list ->
            // mapea a UI
            val ui = list.map {
                CartUiItem(
                    id = it.id,
                    productId = it.productId,
                    name = it.productName,
                    price = it.unitPrice,
                    quantity = it.qty,
                    imagePath = it.imagePath
                )
            }
            _items.value = ui
            _total.value = ui.sumOf { it.price * it.quantity }
        }
    }

    /** Agregar un producto (1 unidad por defecto) */
    fun addProduct(p: Product, userId: Long) = viewModelScope.launch {
        // cartId es un MutableStateFlow<Long?>
        val cid = cartId.value ?: db.cartDao()
            .getOrCreateActiveCart(userId)
            .also { cartId.value = it }

        val existing = db.cartDao().findItem(cid, p.id)
        if (existing == null) {
            db.cartDao().insertItem(
                CartItemEntity(
                    cartId = cid,
                    productId = p.id,
                    qty = 1,
                    unitPrice = p.price,
                    productName = p.name,
                    imagePath = p.imagePath
                )
            )
        } else {
            db.cartDao().updateQty(existing.id, existing.qty + 1)
        }
    }

    fun increase(item: CartUiItem) = viewModelScope.launch {
        db.cartDao().updateQty(item.id, item.quantity + 1)
    }

    fun decrease(item: CartUiItem) = viewModelScope.launch {
        val newQ = (item.quantity - 1).coerceAtLeast(1)
        db.cartDao().updateQty(item.id, newQ)
    }

    fun remove(item: CartUiItem) = viewModelScope.launch {
        db.cartDao().deleteItem(item.id)
    }

    fun clear() = viewModelScope.launch {
        cartId.value?.let { db.cartDao().clearCart(it) }
    }

    // Factory para viewModel()
    class Factory(private val db: AppDb) : androidx.lifecycle.ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CartViewModel(db) as T
        }
    }
}
