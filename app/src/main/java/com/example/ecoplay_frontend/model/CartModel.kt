package com.example.ecoplay_frontend.model

import com.google.gson.annotations.SerializedName

data class CartModel(
    @SerializedName("_id") val cartId: String,  // Assuming the cartId should be a String since the ID in JSON is alphanumeric
    @SerializedName("product") val products: List<ProductModel>,  // This should be a list of product IDs (strings)
    @SerializedName("totalC") var totalC: Int,  // Changed the variable name to match JSON key and type

)

