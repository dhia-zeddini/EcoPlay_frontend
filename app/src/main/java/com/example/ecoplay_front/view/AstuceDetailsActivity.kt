package com.example.ecoplay_front.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ecoplay_front.model.Astuce
import com.example.ecoplay_front.R
import com.example.ecoplay_front.R.id.info
import com.example.ecoplay_front.uttil.Constants.BASE_URL

class AstuceDetailsActivity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astuce_details)

        val astuce = intent.getSerializableExtra("ASTUCE") as? Astuce
        val imageView : ImageView =findViewById(R.id.imageA)
        val title: TextView = findViewById(R.id.titleAstuce)
        val link :TextView =findViewById(R.id.linkA)
        val info :TextView = findViewById(R.id.textView2)

        link.setOnClickListener {
            val link = link.text.toString()

            // Check if the link is not empty
            if (link.isNotEmpty()) {
                // Create an Intent with the ACTION_VIEW action
                val intent = Intent(Intent.ACTION_VIEW)

                // Parse the link string to a Uri object
                val uri = Uri.parse(link)

                // Set the data of the Intent to the Uri
                intent.data = uri

                // Start the activity with the Intent
                startActivity(intent)
            }
        }
        info.text=astuce?.informationsA
        title.text = astuce?.titleA
        link.text =astuce?.linkA
        Glide.with(this)
            .load("${BASE_URL}images/astuce/${astuce?.imageDetailA}")
            .into(imageView)
        // Use 'astuce' to display details in the activity
    }
}