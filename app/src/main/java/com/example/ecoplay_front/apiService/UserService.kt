package com.example.ecoplay_front.apiService
import com.example.ecoplay_front.model.LoginRespenseModel
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService  {
    @POST("login")
    fun login(@Body phone: LoginRequestModel): Call<LoginRespenseModel>

    @POST("registration")
    fun register(@Body phone: RegisterRequestModel): Call<RegisterResponse>


    @POST("forgetPwd")
    fun forgetPwd(@Body phone: LoginRequestModel): Call<LoginRespenseModel>

    @POST("otp")
    fun otp(@Header("token") token: String, @Body code: LoginRequestModel): Call<LoginRespenseModel>

}