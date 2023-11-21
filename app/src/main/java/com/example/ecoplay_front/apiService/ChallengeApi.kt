package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.model.Comment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ChallengeApi {
    @GET("api/challenges")
    fun listChallenges(): Call<List<Challenge>>

    @POST("api/challenges/{id}/join")
    fun joinChallenge(@Path("id") challengeId: String, @Body userId: Map<String, String>): Call<ResponseBody>


    @GET("api/challenges/comments/{id}")
    fun getComments(@Path("id") challengeId: String?): Call<List<Comment>>

    @Multipart
    @POST("api/challenges/comments/{id}")
    fun postComment(
        @Path("id") challengeId: String,
        @Part("userId") userId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<ResponseBody>







}
