package com.example.ecoplay_front.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.LoginResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPwdViewModel :ViewModel(){

    val responseLiveData = MutableLiveData<LoginResponseModel>()
    val errorMessage = MutableLiveData<String>()

    fun newPwd(token: String, newPwd: String) {
        val apiService = UserService.create()
        val requestModel = LoginRequestModel("", newPwd)
        Log.d("RetrofitCall", "Response successful: "+requestModel)
        apiService.newPwd("Bearer $token", requestModel).enqueue(object :
            Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()
                    Log.d("RetrofitCall", "Response successful: ${response.code()}")

                } else if (response.code()==404){
                    errorMessage.value = "Error: User Not Found"
                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }
}