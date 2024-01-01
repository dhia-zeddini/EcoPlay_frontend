package com.example.ecoplay_front.model

import java.io.Serializable

data class ProductModel(
    val _id: String,
    val nameP: String,
    val descriptionP: String,
    val image: String,
    val priceP: String,
    val typeP: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int

): Serializable

