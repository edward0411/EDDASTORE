package com.example.eddastore.data.local.dao

import androidx.room.*
import com.example.eddastore.data.local.entity.OrderEntity
import com.example.eddastore.data.local.entity.OrderItemEntity
import com.example.eddastore.data.local.model.OrderWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    @Insert
    suspend fun insertItems(items: List<OrderItemEntity>)

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeOrders(userId: Long): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrder(orderId: Long): OrderWithItems?
}
