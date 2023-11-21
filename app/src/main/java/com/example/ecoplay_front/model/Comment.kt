package com.example.ecoplay_front.model

import java.util.Date

data class Comment(
    val id: String,
    val user: String,
    val title: String,
    val description: String,
    val image: String?,
    val rating: Int,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)