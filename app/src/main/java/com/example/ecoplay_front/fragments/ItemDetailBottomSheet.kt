package com.example.ecoplay_front.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.Challenge
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ItemDetailBottomSheet : BottomSheetDialogFragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            val title = bundle.getString("title")
            val description = bundle.getString("description")
            val imageUrl = bundle.getString("imageUrl")
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

            // 7adher taswyra bel glide
            Glide.with(this)
                .load(imageUrl)
                .into(view.findViewById<ImageView>(R.id.bottom_sheet_imageView))
        }
    }


    companion object {
        fun newInstance(challenge: Challenge): ItemDetailBottomSheet {
            val fragment = ItemDetailBottomSheet()
            val args = Bundle().apply {
                putString("title", challenge.title)
                putString("description", challenge.description)
                putString("imageUrl", challenge.media)
                putString("startDate", challenge.start_date)
                putString("endDate", challenge.end_date)
                putInt("point_value", challenge.point_value)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
