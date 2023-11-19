package com.example.ecoplay_front.repository

import com.example.ecoplay_front.apiService.ChallengeApi
import com.example.ecoplay_front.model.Challenge
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChallengeRepository {
    private val service: ChallengeApi by lazy {
        RetrofitService.createService(ChallengeApi::class.java)
    }

    fun listChallenges(callback: Callback<List<Challenge>>) {
        service.listChallenges().enqueue(callback)
    }

    fun joinChallenge(challengeId: String, callback: Callback<ResponseBody>) {
        val userIdBody = mapOf("userId" to "507f1f77bcf86cd799439011")
        service.joinChallenge(challengeId, userIdBody).enqueue(callback)
    }


    object RetrofitService {
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("http://192.168.1.115:9001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun <T> createService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }
    }
}
