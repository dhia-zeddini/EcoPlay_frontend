package com.example.ecoplay_front.view

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import com.example.ecoplay_front.MainActivity
import com.example.ecoplay_front.MusicService
import com.example.ecoplay_front.R
import nl.joery.animatedbottombar.AnimatedBottomBar

class HomePageActivity : AppCompatActivity() {
    private lateinit var bottomBar: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        startService(Intent(this, MusicService::class.java))


        bottomBar = findViewById(R.id.bottom_bar)
        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            fun onTabSelected(index: Int, oldTab: AnimatedBottomBar.Tab?, newTab: AnimatedBottomBar.Tab) {
                when (newTab.id) {
                    R.id.tab_challenges -> startChallengesActivity()
                    R.id.tab_alarm -> startAstucesActivity()
                    R.id.tab_home -> startHomeActivity()
                    R.id.tab_bread -> startStoreActivity()
                    R.id.tab_cart -> startProfileActivity()
                }
            }

            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newTab.id) {
                    R.id.tab_challenges -> startChallengesActivity()
                    R.id.tab_alarm -> startAstucesActivity()
                    R.id.tab_home -> MainActivity()
                    R.id.tab_bread -> startStoreActivity()
                    R.id.tab_cart -> startProfileActivity()
                }            }
        })

        styleTextView(findViewById(R.id.title_daily_competitions), "Eco ", "Play")
    }

    private fun startChallengesActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startAstucesActivity() {
        val intent = Intent(this, AstuceHomeActivity::class.java)
        startActivity(intent)
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomePageActivity::class.java)
        startActivity(intent)
    }

    private fun startStoreActivity() {
        val intent = Intent(this, CarouselMain::class.java)
        startActivity(intent)
    }

    private fun startProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }


    private fun styleTextView(textView: TextView, partOne: String, partTwo: String) {
        val spannableString = SpannableString(partOne + partTwo)
        val blackSpan = ForegroundColorSpan(Color.BLACK)
        val greenSpan = ForegroundColorSpan(Color.parseColor("#44F1A6"))

        spannableString.apply {
            setSpan(blackSpan, 0, partOne.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(greenSpan, partOne.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView.text = spannableString
    }
}