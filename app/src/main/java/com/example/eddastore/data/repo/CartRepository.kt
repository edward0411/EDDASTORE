// com/example/eddastore/data/repo/CartRepository.kt
package com.example.eddastore.data.repo

import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.local.entity.CartItemEntity
import com.example.eddastore.data.local.entity.CartWithItems
import com.example.eddastore.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.max

class CartRepository(private val db: AppDb) {

    private val cartDao = db.cartDao()

    // ----- Carrito activo -----
    suspend fun activeCartId(userId: Long): Long =
        cartDao.getOrCreateActiveCart(userId)

    // ----- Lecturas -----
    fun observeItems(cartId: Long): Flow<List<CartItemEntity>> =
        cartDao.observeItems(cartId)

    fun observeCart(cartId: Long): Flow<CartWithItems> =
        cartDao.observeCartWithItems(cartId)

    fun observeTotal(cartId: Long): Flow<Double> =
        observeItems(cartId).map { list -> list.sumOf { it.unitPrice * it.qty } }

    // ----- Mutaciones -----
    suspend fun addOrIncrease(cartId: Long, p: Product, qty: Int = 1) {
        val item = cartDao.findItem(cartId, p.id)
        if (item == null) {
            cartDao.insertItem(
                CartItemEntity(
                    cartId = cartId,
                    productId = p.id,
                    qty = qty,
                    unitPrice = p.price,
                    productName = p.name,
                    imagePath = p.imagePath
                )
            )
        } else {
            cartDao.updateQty(item.id, item.qty + qty)
        }
    }

    suspend fun increase(item: CartItemEntity, step: Int = 1) {
        cartDao.updateQty(item.id, item.qty + step)
    }

    suspend fun decrease(item: CartItemEntity, step: Int = 1) {
        val newQty = max(1, item.qty - step) // evita 0; si quieres 0 => eliminar
        cartDao.updateQty(item.id, newQty)
    }

    suspend fun setQty(item: CartItemEntity, qty: Int) {
        cartDao.updateQty(item.id, max(1, qty))
    }

    suspend fun remove(item: CartItemEntity) {
        cartDao.deleteItem(item.id)
    }

    suspend fun clear(cartId: Long) {
        cartDao.clearCart(cartId)
    }
}
