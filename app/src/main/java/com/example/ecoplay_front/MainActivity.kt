package com.example.ecoplay_front

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ecoplay_front.view.WheelActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "Starting WheelActivity")

        val intent = Intent(this, WheelActivity::class.java)
        startActivity(intent)
    }
}
