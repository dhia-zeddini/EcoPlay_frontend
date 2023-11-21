package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.Challenge


import retrofit2.Call
import retrofit2.http.GET

interface ChallengeApi {
    @GET("api/challenges")
    fun listChallenges(): Call<List<Challenge>>
}
