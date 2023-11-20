package com.exemple.ecoplay_front.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.ecoplay_front.model.Astuce
import com.exemple.ecoplay_front.R
import org.w3c.dom.Text

class AstuceDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astuce_details)

        val astuce = intent.getSerializableExtra("ASTUCE") as? Astuce

        val titleA:TextView=findViewById(R.id.titleAstuce)
        val descA:TextView=findViewById(R.id.descA)
        val linkA:TextView=findViewById(R.id.linkA)


       titleA.text=astuce?.titleA
        descA.text=astuce?.imageDetailA
        linkA.text=astuce?.linkA


    }


}

