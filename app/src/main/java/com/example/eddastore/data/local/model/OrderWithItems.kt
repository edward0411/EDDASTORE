package com.example.eddastore.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.eddastore.data.local.entity.OrderEntity
import com.example.eddastore.data.local.entity.OrderItemEntity

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(parentColumn = "id", entityColumn = "orderId")
    val items: List<OrderItemEntity>
)
