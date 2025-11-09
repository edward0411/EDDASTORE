package com.example.eddastore.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.eddastore.domain.model.Product

@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imagePath: String? = null,
    val isActive: Boolean = true
)
fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    stock = stock,
    imagePath = imagePath,           // ← mapea igual
    isActive = isActive
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    stock = stock,
    imagePath = imagePath,           // ← mapea igual
    isActive = isActive
)
