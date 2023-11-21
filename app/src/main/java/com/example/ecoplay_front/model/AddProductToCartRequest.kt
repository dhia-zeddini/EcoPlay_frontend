package com.example.ecoplay_front.model

data class AddProductToCartRequest(
    val cartId: String,
    val productId: String
)
