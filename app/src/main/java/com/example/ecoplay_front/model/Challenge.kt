package com.example.ecoplay_front.model

data class Challenge(
    val _id: String,
    val title: String,
    val description: String,
    val start_date: String,
    val end_date: String,
    val category: String,
    val point_value: Int,
    val media: String,
    val participants: MutableList<String> = mutableListOf()
)