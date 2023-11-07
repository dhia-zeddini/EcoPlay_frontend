package com.example.ecoplay_front.apiService
import com.example.ecoplay_front.model.UserModel
import retrofit2.Call
import retrofit2.http.GET

interface UserService  {
    @GET("users") // Define your API endpoint for fetching users
    fun getUsers(): Call<List<UserModel>>


}