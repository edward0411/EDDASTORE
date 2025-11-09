package com.example.eddastore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_item")
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,                // FK a orders.id
    val productId: Long,
    // snapshot para histórico (por si cambian luego)
    val productName: String,
    val unitPrice: Double,
    val qty: Int,
    val imagePath: String? = null
)
