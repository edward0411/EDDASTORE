package com.example.eddastore.data.local.entity

import androidx.room.*

@Entity(
    tableName = "cart",
    indices = [Index("userId")]
)
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val status: String = "active",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "cart_item",
    foreignKeys = [ForeignKey(
        entity = CartEntity::class,
        parentColumns = ["id"],
        childColumns = ["cartId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("cartId"), Index("productId")]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cartId: Long,
    val productId: Long,
    val qty: Int,
    val unitPrice: Double,
    val productName: String,
    val imagePath: String? = null
)

data class CartWithItems(
    @Embedded val cart: CartEntity,
    @Relation(parentColumn = "id", entityColumn = "cartId")
    val items: List<CartItemEntity>
)
