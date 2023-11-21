package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.ProductModel
import retrofit2.http.GET

interface CarouselleService {
    @GET("products/getAll")
    suspend fun getAllProducts(): List<ProductModel>
}
