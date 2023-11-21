package com.example.ecoplay_front.model

import com.google.gson.annotations.SerializedName

data class PaymentIntentResponse(
    @SerializedName("client_secret")
    val clientSecret: String?
)
