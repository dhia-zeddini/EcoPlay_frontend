package com.example.ecoplay_front.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecoplay_front.R
import com.example.ecoplay_front.fragments.AchievementsFragment
import com.example.ecoplay_front.fragments.ActivityFragment
import com.example.ecoplay_front.fragments.ProfileFragment
import com.example.ecoplay_front.uttil.Constants.BASE_URL
import com.example.ecoplay_front.viewModel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val accountNav:TextView=findViewById(R.id.accountNav)
        val achievementsNav:TextView=findViewById(R.id.achivementsNav)
        val activityNav:TextView=findViewById(R.id.activityNav)
        val userImage:ImageView=findViewById(R.id.userImage)

        val mSharedPreferences = application.getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        var token:String?=mSharedPreferences.getString(TOKEN,"no token")
        Log.d("RetrofitCall","prefs${token}")

        viewModel.getProfile(token ?: "")

        viewModel.userProfile.observe(this) { userModel ->
            Log.d("RetrofitCallImage", "Profile Response successful: ${userModel.avatar}")

            val imageUri="${BASE_URL}images/avatars/${userModel.avatar}"
            Glide.with(this)
                .load(imageUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userImage)

        }

        accountNav.setOnClickListener{
            changeFragment(ProfileFragment(),"account")
            accountNav.setTextColor(ContextCompat.getColor(this, R.color.blue))
            achievementsNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
            activityNav.setTextColor(ContextCompat.getColor(this, R.color.gray))
        }
        achievementsNav.setOnClickListener{
            changeFragment(AchievementsFragment(),"achievements")
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