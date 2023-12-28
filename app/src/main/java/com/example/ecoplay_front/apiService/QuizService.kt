package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.QuizQuestion
import retrofit2.Call
import retrofit2.http.GET

interface QuizService {
    @GET("QuizR/quizResults")
    fun getQuizQuestion(): Call<List<QuizQuestion>>
}
