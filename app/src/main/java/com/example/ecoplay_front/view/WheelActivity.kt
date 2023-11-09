package com.example.ecoplay_front.view

import android.content.Context
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import com.example.ecoplay_front.R
import com.example.ecoplay_front.R.layout.layout_popup
import kotlin.random.Random

class WheelActivity : AppCompatActivity() {
    private val sectors = arrayOf("Plant a tree", "Run5Km", "Feed Animals", "Spin Again","Walk 1000 steps","Clean Public Place","Collect 100 bottels", "Blood donation")
    private var sectorDegrees = Array(8) {0}
    private val random = Random.Default
    private var degree:Int = 0
    private var isSpinning = false
    private lateinit var wheel: ImageView
    private lateinit var spinBtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel)
        Log.d("Activity Wheel", "HERE 1")
        wheel = findViewById(R.id.wheelcenter)
        spinBtn = findViewById(R.id.spinButton)
        getDegreeForSectors()
        Log.d("Activity Wheel", "HERE 2")
        spinBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if(!isSpinning)
                {
                    spin()
                    isSpinning = true
                }

            }
        })


    }

    private fun spin()
    {
        degree = random.nextInt(sectors.size - 1)
        val rotateAnimation = RotateAnimation(0f, (360 * sectors.size) + sectorDegrees[degree].toFloat(), RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 3600
        rotateAnimation.fillAfter = true
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                // Implementation for onAnimationStart
                // Your code when the animation starts
            }

            override fun onAnimationEnd(animation: Animation) {


                    Toast.makeText(this@WheelActivity, "You've got " + sectors[sectors.size - degree - 1], Toast.LENGTH_SHORT).show()
                isSpinning = false
            }

            override fun onAnimationRepeat(animation: Animation) {
                // Implementation for onAnimationRepeat
                // Your code when the animation is repeated
            }
        })

        wheel.startAnimation(rotateAnimation)
    }

    private fun getDegreeForSectors(){
        val sectorDegree = 360 / sectors.size
        for(i in 0..7)
        {
            sectorDegrees[i] = (i + 1) * sectorDegree
        }
    }

}