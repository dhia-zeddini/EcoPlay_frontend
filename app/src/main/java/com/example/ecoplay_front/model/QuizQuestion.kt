package com.example.ecoplay_front.model

data class QuizQuestion(
    val _id: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>,
    val __v: Int
)

