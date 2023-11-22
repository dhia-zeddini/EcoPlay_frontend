package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.Astuce
import retrofit2.Call
import retrofit2.http.GET

interface AstuceApi {
    @GET("astuce/getall")
    fun listastuce(): Call<List<Astuce>>
}
