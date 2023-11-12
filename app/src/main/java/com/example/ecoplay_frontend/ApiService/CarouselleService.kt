package com.example.ecoplay_frontend.ApiService

import com.example.ecoplay_frontend.model.ProductModel
import retrofit2.http.GET

interface CarouselleService {
    @GET("products/getAll")
    suspend fun getAllProducts(): List<ProductModel>
}
