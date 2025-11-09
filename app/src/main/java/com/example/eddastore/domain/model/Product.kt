package com.example.eddastore.domain.model

data class Product(
    val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imagePath: String? = null,
    val isActive: Boolean = true
)