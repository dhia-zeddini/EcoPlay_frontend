package com.example.ecoplay_front.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.view.PREF_FILE
import com.example.ecoplay_front.view.TOKEN
import com.example.ecoplay_front.viewModel.ProfileViewModel


class AchievementsFragment : Fragment() {


    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_achivements, container, false)

        var progressBar: ProgressBar =view.findViewById(R.id.progressBar)
        var levelMainIcon: TextView =view.findViewById(R.id.levelMainIcon)
        var levelHeader: TextView =view.findViewById(R.id.levelHeader)
        var levelSecondIcon: TextView =view.findViewById(R.id.levelSecondIcon)
        var tvPoints: TextView =view.findViewById(R.id.tvPoints)
        var tvNbrGold: TextView =view.findViewById(R.id.tvNbrGold)
        var tvNbrSilver: TextView =view.findViewById(R.id.tvNbrSilver)
        var tvNbrBronze: TextView =view.findViewById(R.id.tvNbrBronze)
        var pointsIndicator: TextView =view.findViewById(R.id.pointsIndicator)
        var nextLevelSecondIcon: TextView =view.findViewById(R.id.nextLevelSecondIcon)

        val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        var token:String?=mSharedPreferences.getString(TOKEN,"no token")
        Log.d("RetrofitCall","prefs${token}")

        viewModel.getProfile(token ?: "")

        viewModel.userProfile.observe(viewLifecycleOwner) { userModel ->
            levelMainIcon.text = userModel.level.toString()
            levelSecondIcon.text = userModel.level.toString()
            nextLevelSecondIcon.text = (userModel.level+1).toString()
            tvPoints.text = userModel.score.toString()
            levelHeader.text = "Level ${userModel.level}"
            pointsIndicator.text = "${userModel.score} Points to next level"
            progressBar.progress = userModel.score
            tvNbrGold.text= userModel.goldMedal.toString()
            tvNbrSilver.text=userModel.silverMedal.toString()
            tvNbrBronze.text=userModel.bronzeMedal.toString()
        }


        return view
    }


}