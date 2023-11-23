package com.example.ecoplay_front.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.repository.ChallengeRepository
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeViewModel : ViewModel() {
    private val challenges: MutableLiveData<List<Challenge>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()

    private val repository = ChallengeRepository()

    fun getChallenges(): LiveData<List<Challenge>> = challenges
    fun getErrorMessage(): LiveData<String> = errorMessage

    fun loadChallenges() {
        repository.listChallenges(object : Callback<List<Challenge>> {
            override fun onResponse(call: Call<List<Challenge>>, response: Response<List<Challenge>>) {
                if (response.isSuccessful) {
                    challenges.value = response.body()
                } else {
                    errorMessage.value = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<List<Challenge>>, t: Throwable) {
                errorMessage.value = "Network Error: ${t.message}"
            }
        })
    }

    fun joinChallenge(token:String,challengeId: String) {
        repository.joinChallenge(token,challengeId, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // Handle the successful joining here if needed
                } else {
                    val message = if (response.code() == 400) {
                        "You are already in the challenge"
                    } else {
                        "Failed to join challenge: ${response.code()}"
                    }
                    errorMessage.value = message
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                errorMessage.value = "Network error: ${t.message}"
            }
        })
    }
}
