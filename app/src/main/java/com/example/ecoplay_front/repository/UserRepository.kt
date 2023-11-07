package com.example.ecoplay_front.repository

import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class UserRepository {

    private val userService: UserService = TODO()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:9001/user") // Replace with your API base URL
            .build()

        userService = retrofit.create(UserService::class.java)
    }

    fun getUsers(callback: (List<UserModel>?) -> Unit) {
        val call = userService.getUsers()

        call.enqueue(object : Callback<List<UserModel>> {
            override fun onResponse(call: Call<List<UserModel>>, response: Response<List<UserModel>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<UserModel>>, t: Throwable) {
                callback(null)
            }
        })
    }
}