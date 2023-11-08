package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.LoginRespenseModel
import com.example.ecoplay_front.model.UserModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST



interface apiTest {
    @POST("login")
    fun login(@Body phone: UserModel): Call<LoginRespenseModel>

}
