package com.example.ecoplay_frontend.model

import java.io.Serializable

data class ProductModel(
    val _id: String,
    val nameP: String,
    val descriptionP: String,
    val priceP: Double,
    val typeP: String,
    val image: String
): Serializable

