package com.example.ecoplay_front

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.example.ecoplay_front.adapter.ChallengeAdapter
import com.example.ecoplay_front.fragments.ItemDetailBottomSheet
import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.view.ActivityCommunity
import com.example.ecoplay_front.viewModel.ChallengeViewModel

class MainActivity : AppCompatActivity(), ChallengeAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter
    private lateinit var viewModel: ChallengeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setupViewModel()
        styleTextView(findViewById(R.id.title_daily_competitions), "Daily ", "Competitions")
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.challenge_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        challengeAdapter = ChallengeAdapter(emptyList(), this)
        recyclerView.adapter = challengeAdapter
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[ChallengeViewModel::class.java]

        viewModel.getChallenges().observe(this, { challenges ->
            challenges?.let {
                challengeAdapter.setChallenges(it)
            } ?: run {
                showSnackbar("Failed to load challenges.")
            }
        })

        viewModel.getErrorMessage().observe(this, { errorMessage ->
            showSnackbar(errorMessage)
        })

        viewModel.loadChallenges()
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }

    private fun styleTextView(textView: TextView, partOne: String, partTwo: String) {
        val spannableString = SpannableString(partOne + partTwo)
        val blackSpan = ForegroundColorSpan(Color.BLACK)
        val greenSpan = ForegroundColorSpan(Color.parseColor("#44F1A6"))

        spannableString.apply {
            setSpan(blackSpan, 0, partOne.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(greenSpan, partOne.length, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        textView.text = spannableString
    }

    override fun onItemClick(challenge: Challenge) {
        val bottomSheet = ItemDetailBottomSheet.newInstance(challenge)
        bottomSheet.show(supportFragmentManager, "itemDetail")
    }
}
