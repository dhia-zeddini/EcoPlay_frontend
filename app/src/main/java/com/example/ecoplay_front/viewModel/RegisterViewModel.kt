package com.example.ecoplay_front.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.apiService.UserService
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterViewModel : ViewModel() {
    val registerResponse = MutableLiveData<RegisterResponse>()
    val errorMessage = MutableLiveData<String>()

    fun register(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String,
        avatarPart: MultipartBody.Part?
    ) {
        Log.d("RegisterDebug", "register() called")

        // Create an instance of your Retrofit API service
        val apiService = UserService.create()

        // Create RequestBody instances for various fields
        val firstNameRequestBody = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val lastNameRequestBody = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val emailRequestBody = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val phoneNumberRequestBody = phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        val passwordRequestBody = password.toRequestBody("text/plain".toMediaTypeOrNull())

        // Now make the Retrofit call
        val call = apiService.register(
            firstNameRequestBody,
            lastNameRequestBody,
            emailRequestBody,
            phoneNumberRequestBody,
            passwordRequestBody,
            avatarPart // Use the MultipartBody.Part here
        )

        Log.d("RegisterDebug", "Retrofit call initiated")

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                Log.d("RegisterDebug", "Response received: ${response.code()}")
                if (response.isSuccessful) {
                    // If the response is successful, set the registerResponse LiveData
                    registerResponse.value = response.body()
                } else if (response.code() == 403) {
                    // Handle specific error code (e.g., account already exists)
                    errorMessage.value = "Error: Account already exists"
                } else {
                    // Handle other error cases (e.g., response error body)
                    Log.d("RegisterDebug", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Handle failure, set an error message
                Log.e("RegisterDebug", "Retrofit call failed", t)
                errorMessage.value = t.message ?: "Unknown error"
            }
        })
    }

}