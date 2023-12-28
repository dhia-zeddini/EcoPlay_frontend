package com.example.ecoplay_front.view


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.RetrofitInstance
import com.example.ecoplay_front.apiService.RetrofitService
import com.example.ecoplay_front.databinding.ActivityQuizBinding
import com.example.ecoplay_front.model.QuizQuestion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var allowPlaying = true
    private var timer: CountDownTimer? = null
    private var quizList: List<QuizQuestion> = emptyList()
    private var currentIndex = 0

    private var correctCount = 0
    private var incorrectCount = 0
    private var totalQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("QuizActivity", "onCreate called")

        fetchQuizData()
        setupAnswerButtons()
        Log.d("ResultActivity", "Correct: $correctCount, Incorrect: $incorrectCount")


    }

    private fun fetchQuizData() {
        RetrofitInstance.quizService.getQuizQuestion().enqueue(object : Callback<List<QuizQuestion>> {
            override fun onResponse(call: Call<List<QuizQuestion>>, response: Response<List<QuizQuestion>>) {
                Log.d("jawna mrygel", "Response received.")
                if (response.isSuccessful) {
                    val quizzes = response.body()
                    if (!quizzes.isNullOrEmpty()) {
                        quizList = quizzes
                        Log.d("QuizActivity", "Quiz data loaded, displaying questions.")
                        displayQuestion(currentIndex)
                    } else {
                        Log.e("QuizActivity", "Quiz list is empty or null.")
                    }
                } else {
                    Log.e("QuizActivity", "Failed to get quiz data. Code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<QuizQuestion>>, t: Throwable) {
                Log.e("jawna mech mrygel", "Error fetching quiz data: ${t.message}", t)
            }
        })
    }

    private fun displayQuestion(index: Int) {
        if (quizList.isNotEmpty() && index in quizList.indices) {
            val quiz = quizList[index]
            val answers = (listOf(quiz.correct_answer) + quiz.incorrect_answers).shuffled()

            with(binding) {
                tvQuestion.text = quiz.question
                option1.text = answers.getOrNull(0) ?: "" // Use getOrNull to avoid IndexOutOfBoundsException
                option2.text = answers.getOrNull(1) ?: ""
                option3.text = answers.getOrNull(2) ?: ""
                option4.text = answers.getOrNull(3) ?: ""
                tvProgress.text = getString(R.string.quiz_progress, index + 1, quizList.size)
                pbProgress.progress = ((index + 1).toFloat() / quizList.size * 100).toInt()
            }
        } else {

            with(binding) {
                tvQuestion.text = getString(R.string.error_loading_question)
                option1.text = ""
                option2.text = ""
                option3.text = ""
                option4.text = ""
                tvProgress.text = getString(R.string.quiz_progress, 0, 0)
                pbProgress.progress = 0
            }
        }
    }


    private fun setupAnswerButtons() {
        val redBg = ContextCompat.getDrawable(this, R.drawable.red_button_bg)
        val greenBg = ContextCompat.getDrawable(this, R.drawable.blue_button_bg)

        val optionClickListener = View.OnClickListener { view ->
            if (allowPlaying && quizList.isNotEmpty() && currentIndex in quizList.indices) {
                timer?.cancel()
                val isCorrect = (view as? Button)?.text == quizList[currentIndex].correct_answer
                view.background = if (isCorrect) greenBg else redBg
                allowPlaying = false

                // Mettez à jour les compteurs ici en fonction de la réponse
                if (isCorrect) {
                    correctCount++
                } else {
                    incorrectCount++
                }
                totalQuestions++
            }
        }

        with(binding) {
            option1.setOnClickListener(optionClickListener)
            option2.setOnClickListener(optionClickListener)
            option3.setOnClickListener(optionClickListener)
            option4.setOnClickListener(optionClickListener)

            btnNext.setOnClickListener {
                if (quizList.isNotEmpty() && currentIndex < quizList.size - 1) {
                    currentIndex++
                    displayQuestion(currentIndex)
                    resetButtonBackgrounds()
                    allowPlaying = true
                } else {
                    handleQuizEnd()
                }
            }
        }
    }

    private fun resetButtonBackgrounds() {
        val defaultBg = ContextCompat.getDrawable(this, R.drawable.background_btn)
        with(binding) {
            option1.background = defaultBg
            option2.background = defaultBg
            option3.background = defaultBg
            option4.background = defaultBg
        }
    }

    private fun handleQuizEnd() {
        Log.d("QuizActivity", "End of quiz reached.")


        val intent = Intent(this@QuizActivity, ResultActivity::class.java).apply {
            putExtra("correctCount", correctCount)
            putExtra("incorrectCount", incorrectCount)
            putExtra("totalQuestions", totalQuestions)
            // Ajoutez d'autres données nécessaires ici
        }

        startActivity(intent)
        finish()
    }
}

