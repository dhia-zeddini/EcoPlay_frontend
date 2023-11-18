package com.example.ecoplay_front.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ecoplay_front.R
import com.example.ecoplay_front.fragments.AchivementsFragment
import com.example.ecoplay_front.fragments.ActivityFragment
import com.example.ecoplay_front.fragments.ProfileFragment

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val accountNav:TextView=findViewById(R.id.accountNav)
        val achievementsNav:TextView=findViewById(R.id.achivementsNav)
        val activityNav:TextView=findViewById(R.id.activityNav)



        accountNav.setOnClickListener{
            changeFragment(ProfileFragment(),"account")
            accountNav.setTextColor(ContextCompat.getColor(this, R.color.blue))
            achievementsNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
            activityNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
        }
        achievementsNav.setOnClickListener{
            changeFragment(AchivementsFragment(),"achievements")
            accountNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
            achievementsNav.setTextColor(ContextCompat.getColor(this, R.color.blue))
            activityNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
        }
        activityNav.setOnClickListener{
            changeFragment(ActivityFragment(),"activity")
            accountNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
            achievementsNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
            activityNav.setTextColor(ContextCompat.getColor(this, R.color.blue))
        }

    }
    private fun changeFragment(fragment: Fragment, tag: String) {

        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()

        if(tag.isNotEmpty()){
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment).commit()
    }
}