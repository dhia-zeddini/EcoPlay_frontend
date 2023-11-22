package com.example.ecoplay_front.model


import java.io.Serializable


data class Astuce(
    val id: String,
    val titleA: String,
    val sujetA: String,
    val informationsA: String,
    val level: Int,
    val imageDetailA: String,
    val imageItemA: String,
    val linkA: String

): Serializable