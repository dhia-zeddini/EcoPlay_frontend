package com.example.ecoplay_front.viewModel

import CommunityRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.apiService.ChallengeApi
import com.example.ecoplay_front.model.Comment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CommunityViewModel(private val challengeApi: ChallengeApi) : ViewModel() {
    private val comments = MutableLiveData<List<Comment>>()
    private val isLoading = MutableLiveData<Boolean>()
    private val message = MutableLiveData<String>()
    private val communityRepository =
        CommunityRepository(challengeApi) // Instantiate the repository

    fun getCommentsLiveData(): LiveData<List<Comment>> = comments
    fun getIsLoadingLiveData(): LiveData<Boolean> = isLoading
    fun getMessageLiveData(): LiveData<String> = message

    fun fetchComments(challengeId: String) {
        isLoading.value = true
        challengeApi.getComments(challengeId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                isLoading.value = false
                if (response.isSuccessful) {
                    comments.value = response.body() ?: emptyList()
                } else {
                    message.value = "Error getting comments: ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                isLoading.value = false
                message.value = "Failure getting comments: ${t.message}"
            }
        })
    }

    fun postComment(
        challengeId: String,
        userId: String,
        title: String,
        description: String,
        imageFile: File?,
        onSuccess: (Boolean) -> Unit
    ) {
        isLoading.value = true
        communityRepository.postComment(
            challengeId,
            userId,
            title,
            description,
            imageFile,
            onSuccess = {
                isLoading.value = false
                if (it) {
                    fetchComments(challengeId) // Refresh comments after posting
                } else {
                    message.value = "Error posting comment"
                }
                onSuccess(it)
            },
            onError = { error ->
                isLoading.value = false
                message.value = error
                onSuccess(false)
            })
    }
}