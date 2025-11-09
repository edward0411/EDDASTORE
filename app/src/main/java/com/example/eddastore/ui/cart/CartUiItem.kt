package com.example.eddastore.ui.cart

data class CartUiItem(
    val id: Long,          // id del CartItem o, si prefieres, del Product
    val productId: Long,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imagePath: String?
)
