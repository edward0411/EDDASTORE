package com.example.eddastore.data.local.entity

import androidx.room.*

@Entity(tableName = "orders", indices = [Index("userId")])
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val paymentMethod: String,      // "card" | "nequi"
    val total: Double,
    val status: String = "paid",
    val createdAt: Long = System.currentTimeMillis()
)
