package com.example.ecoplay_front.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.apiService.UserService
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    val registerResponse = MutableLiveData<RegisterResponse>()
    val errorMessage = MutableLiveData<String>()

    fun register(registerRequestModel: RegisterRequestModel) {
        val apiService = UserService.create()

        apiService.register(registerRequestModel).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    registerResponse.value = response.body()
                } else if (response.code() == 403) {
                    errorMessage.value = "Error: Account already exist "
                }
                Log.d("RetrofitCall", "Response not successful: ${response.code()}")

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }
}
