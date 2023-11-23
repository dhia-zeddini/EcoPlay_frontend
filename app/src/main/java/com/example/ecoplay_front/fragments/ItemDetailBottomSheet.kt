package com.example.ecoplay_front.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.uttil.Constants
import com.example.ecoplay_front.view.ActivityCommunity
import com.example.ecoplay_front.view.PREF_FILE
import com.example.ecoplay_front.view.TOKEN
import com.example.ecoplay_front.viewModel.ChallengeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ItemDetailBottomSheet : BottomSheetDialogFragment() {

    private lateinit var viewModel: ChallengeViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChallengeViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val joinChallengeButton: Button = view.findViewById(R.id.bottom_sheet_actionButton)


        val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        var token:String?=mSharedPreferences.getString(TOKEN,"no token")
        joinChallengeButton.setOnClickListener {
            val challengeId = arguments?.getString("challengeId")
            challengeId?.let { id ->
                viewModel.joinChallenge("Bearer $token",id)
            }
        }

        // Observing the ViewModel for changes
        viewModel.getErrorMessage().observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show()
                if (errorMessage == "You are already in the challenge") {
                    startCommunityActivity()
                }
            }
        }


        arguments?.let { bundle ->
            val title = bundle.getString("title")
            val description = bundle.getString("description")
            val challengeMedia = bundle.getString("media")
            Log.d("ImageLoadDebug", "Media path: $challengeMedia")
            val startDateString = bundle.getString("startDate")
            val endDateString = bundle.getString("endDate")

            val pointValue = bundle.getInt("point_value")


            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")

            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")

            try {
                val startDate = inputFormat.parse(startDateString)
                val endDate = inputFormat.parse(endDateString)
                view.findViewById<TextView>(R.id.bottom_sheet_titleTextView).text = title
                view.findViewById<TextView>(R.id.bottom_sheet_descriptionTextView).text = description

                startDate?.let {
                    view.findViewById<TextView>(R.id.bottom_sheet_startDateTextView).text = outputFormat.format(it)
                }

                endDate?.let {
                    view.findViewById<TextView>(R.id.bottom_sheet_endDateTextView).text = outputFormat.format(it)
                }

                view.findViewById<TextView>(R.id.bottom_sheet_pointValueTextView).text = getString(R.string.points_format, pointValue)

            } catch (e: ParseException) {
                e.printStackTrace()
            }


            val imageUrl = "${Constants.BASE_URL}images/challenges/${challengeMedia}"
            Glide.with(this)
                .load(imageUrl)
                .into(view.findViewById<ImageView>(R.id.bottom_sheet_imageView))

        }
    }

    private fun startCommunityActivity() {
        val challengeId = arguments?.getString("challengeId") ?: return
        val intent = Intent(requireContext(), ActivityCommunity::class.java)
        intent.putExtra("challengeId", challengeId)
        startActivity(intent)
    }


    companion object {
        fun newInstance(challenge: Challenge): ItemDetailBottomSheet {
            val fragment = ItemDetailBottomSheet()
            val args = Bundle().apply {
                putString("title", challenge.title)
                putString("description", challenge.description)
                putString("media", challenge.media)
                putString("startDate", challenge.start_date)
                putString("endDate", challenge.end_date)
                putInt("point_value", challenge.point_value)
                putString("challengeId", challenge._id) // Add challenge ID here
            }
            fragment.arguments = args
            return fragment
        }
    }
}