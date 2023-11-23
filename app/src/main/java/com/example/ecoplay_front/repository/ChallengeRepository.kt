package com.example.ecoplay_front.repository

import com.example.ecoplay_front.apiService.ChallengeApi
import com.example.ecoplay_front.apiService.RetrofitService
import com.example.ecoplay_front.model.Challenge
import okhttp3.ResponseBody
import retrofit2.Callback

class ChallengeRepository {
    private val service: ChallengeApi by lazy {
        RetrofitService.createService(ChallengeApi::class.java)
    }

    fun listChallenges(callback: Callback<List<Challenge>>) {
        service.listChallenges().enqueue(callback)
    }

    fun joinChallenge(token:String,challengeId: String, callback: Callback<ResponseBody>) {
        service.joinChallenge(token,challengeId).enqueue(callback)
    }



}
