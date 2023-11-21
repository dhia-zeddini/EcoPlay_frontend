package com.example.ecoplay_front.model

data class CalculateCartTotalResponse(
    val message: String,
    val total: Double,
    val cart: Any
)
