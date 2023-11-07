package com.example.ecoplay_front.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecoplay_front.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_front.apiService.ChallengeApi
import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.viewModel.ChallengeAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChallengesHome : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var challengeAdapter: ChallengeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.challengeshome)

        // Set up RecyclerView and its adapter
        recyclerView = findViewById(R.id.challenge_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        challengeAdapter = ChallengeAdapter()
        recyclerView.adapter = challengeAdapter

        loadChallenges()
    }

    private fun loadChallenges() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.111.31:9001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of your API interface
        val service = retrofit.create(ChallengeApi::class.java)

        // Enqueue the call to asynchronously fetch the challenges
        service.listChallenges().enqueue(object : Callback<List<Challenge>> {
            override fun onResponse(call: Call<List<Challenge>>, response: Response<List<Challenge>>) {
                if (response.isSuccessful) {
                    response.body()?.let { challenges ->
                        challengeAdapter.setChallenges(challenges)
                    }
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Challenge>>, t: Throwable) {
                showError("Network Error: ${t.message}")
            }
        })
    }

    private fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
}
