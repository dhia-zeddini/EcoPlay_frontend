package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.model.RegisterRequestModel
import retrofit2.http.Body
import retrofit2.http.GET

interface CarouselleService {
    @GET("products/getAll")
     fun getAllProducts(): List<ProductModel>

    @GET("products")
    fun getProductById(@Body produitId: String): ProductModel
}
