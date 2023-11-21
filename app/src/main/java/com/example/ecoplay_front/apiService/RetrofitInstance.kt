package com.example.ecoplay_front.apiService
import  com.example.ecoplay_front.apiService.CarouselleService
import com.example.ecoplay_front.uttil.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val carouselleService: CarouselleService by lazy {
        retrofit.create(CarouselleService::class.java)
    }
    val cartService: CartService by lazy {
        retrofit.create(CartService::class.java)
    }
}