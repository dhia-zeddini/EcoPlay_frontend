package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.Astuce
import com.example.ecoplay_front.uttil.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AstuceApi {
    @GET("astuce/getall")
    fun listastuce(): Call<List<Astuce>>

    companion object{



        fun create() : UserService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()

            return retrofit.create(UserService::class.java)
        }
    }
}
