package com.example.ecoplay_front.apiService
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.model.UserModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService  {
    @POST("login")
    fun login(@Body phone: LoginRequestModel): Call<LoginResponseModel>

    @POST("registration")
    fun register(@Body phone: RegisterRequestModel): Call<RegisterResponse>


    @POST("forgetPwd")
    fun forgetPwd(@Body phone: LoginRequestModel): Call<LoginResponseModel>

    @POST("otp")
    fun otp(@Header("token") token: String, @Body code: LoginRequestModel): Call<LoginResponseModel>

    @GET("user/profile")
    fun profile(@Header("token") token: String?): Call<UserModel>

}