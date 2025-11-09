package com.example.eddastore.data.repo

import androidx.room.withTransaction
import com.example.eddastore.data.local.AppDb
import com.example.eddastore.data.local.dao.CartDao
import com.example.eddastore.data.local.dao.OrderDao
import com.example.eddastore.data.local.dao.ProductDao
import com.example.eddastore.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import com.example.eddastore.data.local.model.OrderWithItems


class OrderRepository(
    private val db: AppDb,
    private val cartDao: CartDao,
    private val orderDao: OrderDao,
    private val productDao: ProductDao
) {
    fun observeOrders(userId: Long): Flow<List<OrderWithItems>> =
        orderDao.observeOrders(userId)

    suspend fun checkout(cart: CartWithItems, method: String): Long {
        // Transacción: crea orden, items, descuenta stock, limpia carrito
        return db.withTransaction {
            // revalida stock
            cart.items.forEach {
                val stock = productDao.getStock(it.productId) ?: 0
                require(stock >= it.qty) { "Stock insuficiente en ${it.productName}" }
            }

            val total = cart.items.sumOf { it.qty * it.unitPrice }
            val orderId = orderDao.insertOrder(
                OrderEntity(
                    userId = cart.cart.userId,
                    paymentMethod = method,
                    total = total
                )
            )

            orderDao.insertItems(
                cart.items.map {
                    OrderItemEntity(
                        orderId = orderId,
                        productId = it.productId,
                        qty = it.qty,
                        unitPrice = it.unitPrice,
                        productName = it.productName,
                        imagePath = it.imagePath
                    )
                }
            )

            // descuenta stock
            cart.items.forEach {
                productDao.decrementStock(it.productId, it.qty)
            }

            // limpia carrito
            cartDao.clearItems(cart.cart.id)

            orderId
        }
    }
}
