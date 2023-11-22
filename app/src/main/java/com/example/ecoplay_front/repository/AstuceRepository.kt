package com.example.ecoplay_front.repository

import com.example.ecoplay_front.apiService.AstuceApi
import com.example.ecoplay_front.model.Astuce
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AstuceRepository {
    private val astuceService: AstuceApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.128.65:8088") // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        astuceService = retrofit.create(AstuceApi::class.java)
    }

    fun listAstuces(callback: Callback<List<Astuce>>) {
        astuceService.listastuce().enqueue(callback)
    }


    companion object {
        private var instance: AstuceRepository? = null

        fun getInstance(): AstuceRepository {
            return instance ?: synchronized(this) {
                instance ?: AstuceRepository().also { instance = it }
            }
        }
    }
}
