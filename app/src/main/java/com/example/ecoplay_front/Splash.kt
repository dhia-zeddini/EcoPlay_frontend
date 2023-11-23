package com.example.ecoplay_front

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoplay_front.view.HomePageActivity


class Splash : AppCompatActivity() {
    private val SPLASH_DELAY_MS: Long = 1500 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this@Splash, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY_MS)
    }
}