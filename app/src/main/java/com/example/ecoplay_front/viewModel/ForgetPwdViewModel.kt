package com.example.ecoplay_front.viewModel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.apiService.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPwdViewModel : ViewModel() {
    val responseLiveData = MutableLiveData<LoginResponseModel>()
    val errorMessage = MutableLiveData<String>()

    fun forgetPwd(email: String) {
        val apiService = UserService.create() // Adjust this as per your project
        val requestModel = LoginRequestModel(email, "")

        apiService.forgetPwd(requestModel).enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()
                    Log.d("RetrofitCall", "Response successful: ${response.code()}")

                } else if (response.code()==404){
                    errorMessage.value = "Error: User does not exist"
                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun forgetPwdSms(email: String) {
        val apiService = UserService.create() // Adjust this as per your project
        val requestModel = LoginRequestModel(email, "")

        apiService.forgetPwdSms(requestModel).enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                if (response.isSuccessful) {
                    responseLiveData.value = response.body()
                    Log.d("RetrofitCall", "Response successful: ${response.code()}")

                } else if (response.code()==404){
                    errorMessage.value = "Error: User does not exist"
                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }
}
