package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.UserModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST



interface apiTest {
    @POST("login")
    fun login(@Body user: UserModel): Call<UserModel>

    companion object {
        var BASE_URL = "http://192.168.111.18:9001/"

        fun create(): apiTest {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(apiTest::class.java)
        }
    }
}
