package com.example.ecoplay_front.apiService
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.model.UpdateResponseModel
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.uttil.Constants.BASE_URL
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserService  {
    @POST("login")
    fun login(@Body phone: LoginRequestModel): Call<LoginResponseModel>

    @POST("registration")
    fun register(@Body phone: RegisterRequestModel): Call<RegisterResponse>


    @POST("forgetPwd")
    fun forgetPwd(@Body phone: LoginRequestModel): Call<LoginResponseModel>

    @POST("otp")
    fun otp(@Header("token") token: String, @Body code: LoginRequestModel): Call<LoginResponseModel>

    @POST("newPwd")
    fun newPwd(@Header("token") token: String, @Body code: LoginRequestModel): Call<LoginResponseModel>

    @GET("user/profile")
    fun profile(@Header("token") token: String?): Call<UserModel>

    @PUT("user")
    fun updateProfile(@Header("token") token: String?,@Body phone: RegisterRequestModel): Call<UpdateResponseModel>

    @DELETE("user")
    fun deleteAccount(@Header("token") token: String?): Call<UpdateResponseModel>


    companion object{



        fun create() : UserService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(UserService::class.java)
        }
    }

}