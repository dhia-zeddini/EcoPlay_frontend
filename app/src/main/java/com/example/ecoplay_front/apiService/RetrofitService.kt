package com.example.ecoplay_front.apiService

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ecoplay-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}