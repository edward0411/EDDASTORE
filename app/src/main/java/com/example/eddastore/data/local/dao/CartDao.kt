package com.example.eddastore.data.local.dao

import androidx.room.*
import com.example.eddastore.data.local.entity.CartEntity
import com.example.eddastore.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow
import com.example.eddastore.data.local.entity.CartWithItems

@Dao
interface CartDao {

    // -- Carrito
    @Insert
    suspend fun insertCart(cart: CartEntity): Long

    @Query("SELECT id FROM cart WHERE userId = :userId AND status = 'active' LIMIT 1")
    suspend fun findActiveCartId(userId: Long): Long?

    @Query("SELECT * FROM cart WHERE userId = :userId AND status = 'active' LIMIT 1")
    suspend fun getActiveCart(userId: Long): CartEntity?

    suspend fun getOrCreateActiveCart(userId: Long): Long {
        val existing = findActiveCartId(userId)
        if (existing != null) return existing
        return insertCart(CartEntity(userId = userId))
    }

    // -- Items
    @Query("SELECT * FROM cart_item WHERE cartId = :cartId ORDER BY id DESC")
    fun observeItems(cartId: Long): Flow<List<CartItemEntity>>


    @Query("SELECT * FROM cart_item WHERE cartId = :cartId AND productId = :productId LIMIT 1")
    suspend fun findItem(cartId: Long, productId: Long): CartItemEntity?

    @Insert
    suspend fun insertItem(item: CartItemEntity): Long

    @Query("UPDATE cart_item SET qty = :qty WHERE id = :itemId")
    suspend fun updateQty(itemId: Long, qty: Int)

    @Query("DELETE FROM cart_item WHERE id = :itemId")
    suspend fun deleteItem(itemId: Long)

    @Query("DELETE FROM cart_item WHERE cartId = :cartId")
    suspend fun clearCart(cartId: Long)

    // ----- Carrito + relación de ítems -----
    @Transaction
    @Query("SELECT * FROM cart WHERE id = :cartId LIMIT 1")
    fun observeCartWithItems(cartId: Long): Flow<CartWithItems>
    fun clearItems(id: Long) {}
}
