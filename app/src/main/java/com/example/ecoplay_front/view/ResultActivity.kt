package com.example.ecoplay_front.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoplay_front.R
import com.example.ecoplay_front.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the counts directly from the intent
        val correctCount = intent.getIntExtra("correctCount", 0)
        val incorrectCount = intent.getIntExtra("incorrectCount", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)



        binding.tvCorrectCount.text = "Correct: $correctCount"
        binding.tvIncorrectCount.text = "Incorrect: $incorrectCount"
        binding.tvTotalQuestions.text = "Total: $totalQuestions"
        // Define the OnClickListener for the Home button
        binding.btnHome.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
