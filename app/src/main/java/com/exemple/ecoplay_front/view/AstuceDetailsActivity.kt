package com.exemple.ecoplay_front.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoplay_front.model.Astuce
import com.exemple.ecoplay_front.R

class AstuceDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astuce_details)

        val astuce = intent.getSerializableExtra("ASTUCE") as Astuce
        // Use 'astuce' to display details in the activity
    }
}
