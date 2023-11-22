package com.example.ecoplay_front.viewModel
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.UpdateResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {
    val userProfile = MutableLiveData<UserModel>()
    val updateResponse = MutableLiveData<UpdateResponseModel>()
    val deleteResponse = MutableLiveData<UpdateResponseModel>()
    val errorMessage = MutableLiveData<String>()

    fun getProfile(token: String) {
        val apiService = UserService.create()
        apiService.profile("Bearer $token").enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    userProfile.value = response.body()
                    Log.d("RetrofitCall", "Profile Response successful: ${response.body()}")

                } else if (response.code()==404){
                    errorMessage.value = "Error: User does not exist"
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun updateProfile(token: String, requestModel: RegisterRequestModel) {
        val apiService = UserService.create()
        apiService.updateProfile("Bearer $token", requestModel).enqueue(object : Callback<UpdateResponseModel> {
            override fun onResponse(call: Call<UpdateResponseModel>, response: Response<UpdateResponseModel>) {
                if (response.isSuccessful) {
                    updateResponse.value = response.body()
                    Log.d("RetrofitCall", "Response update body: ${response.code()}")

                } else if (response.code() == 403){
                    errorMessage.value = "Error: You are not authorized"
                }
            }

            override fun onFailure(call: Call<UpdateResponseModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

    fun deleteAccount(token: String) {
        val apiService = UserService.create()
        apiService.deleteAccount("Bearer $token").enqueue(object : Callback<UpdateResponseModel> {
            override fun onResponse(call: Call<UpdateResponseModel>, response: Response<UpdateResponseModel>) {
                if (response.isSuccessful) {
                    deleteResponse.value = response.body()
                    Log.d("RetrofitCall", "Response update body: ${response.code()}")

                }  else if (response.code() == 403){
                    errorMessage.value = "Error: You are not authorized"
                }
            }

            override fun onFailure(call: Call<UpdateResponseModel>, t: Throwable) {
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }}
