package com.example.ecoplay_front.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.ecoplay_front.R

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val privacyBtn: Button? = view.findViewById(R.id.privacyBtn)
        val privacyLayout: LinearLayout? = view.findViewById(R.id.privacyLayout)
        val suffixIconUp = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
        val suffixIconDown = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)

        privacyBtn?.setOnClickListener {
            if (privacyLayout?.isVisible == true) {
                privacyLayout.visibility = View.GONE
                privacyBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconDown, null)
            } else {
                privacyLayout?.visibility = View.VISIBLE
                privacyBtn?.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconUp, null)
            }
        }

        return view
    }
}
