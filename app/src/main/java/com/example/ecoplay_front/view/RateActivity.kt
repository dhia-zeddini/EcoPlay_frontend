package com.example.ecoplay_front.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import com.example.ecoplay_front.R

class RateActivity : AppCompatActivity() {


    private lateinit var ratingBar: RatingBar
    private lateinit var cancelButton: Button
    private lateinit var rateNowButton: Button
    private lateinit var imageRating: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val commentId = intent.getStringExtra("commentId")



        ratingBar = findViewById(R.id.ratingBar)
//        cancelButton = findViewById(R.id.cancel_button)
        rateNowButton = findViewById(R.id.rateNowBtn)
        imageRating = findViewById(R.id.ratingImage)

        ratingBar.stepSize = 1.0f

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            val drawableResource = when (rating.toInt()) {
                1 -> R.drawable.onestar
                2 -> R.drawable.twostar
                3 -> R.drawable.threestar
                4 -> R.drawable.fourstar
                5 -> R.drawable.fivestar
                else -> 0
            }
            if (drawableResource != 0) {
                imageRating.setImageResource(drawableResource)
                animateImage(imageRating)
            }
        }







        setFinishOnTouchOutside(true)


        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        window?.setDimAmount(0.5f)
    }

    private fun animateImage(ratingImage: ImageView) {
        val scaleAnimation = ScaleAnimation(
            0f, 1f,
            0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.fillAfter = true
        scaleAnimation.duration = 200
        ratingImage.startAnimation(scaleAnimation)
    }

}