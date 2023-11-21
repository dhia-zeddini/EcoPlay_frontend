package com.example.ecoplay_front.model

data class UserModel(
    val _id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val avatar: String,
    val points: Int,
    val score: Int,
    val level: Int,
    val goldMedal: Int,
    val silverMedal: Int,
    val bronzeMedal: Int,
    val etatDelete: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

