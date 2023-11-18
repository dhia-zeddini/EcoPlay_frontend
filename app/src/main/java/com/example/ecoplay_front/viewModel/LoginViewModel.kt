package com.example.ecoplay_front.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.LoginResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel: ViewModel() {

    val loginResponse = MutableLiveData<LoginResponseModel>()
    val errorMessage = MutableLiveData<String>()

    fun login(email: String, password: String) {
        val apiService = UserService.create() // Adjust this if you have a different way to instantiate your service
        val call = apiService.login(LoginRequestModel(email, password))

        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                if (response.isSuccessful) {
                    loginResponse.value = response.body()
                } else if (response.code()==404){
                    errorMessage.value = "Error: User does not exist}"
                }else if(response.code()==401) {
                    errorMessage.value = "Error: Invalid password}"
                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}

