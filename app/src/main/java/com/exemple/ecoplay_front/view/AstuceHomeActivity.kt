package com.exemple.ecoplay_front.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import  com.example.ecoplay_front.apiService.AstuceApi
import com.example.ecoplay_front.model.Astuce
import com.exemple.ecoplay_front.R
import com.exemple.ecoplay_front.viewModel.AstuceAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AstuceHomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var astuceAdapter: AstuceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astuce_home)

        // Set up RecyclerView and its adapter
        recyclerView = findViewById(R.id.astuce_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        astuceAdapter = AstuceAdapter()
        recyclerView.adapter = astuceAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.20.10.2:8088/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of your API interface
        val service = retrofit.create(AstuceApi::class.java)

        // Enqueue the call to asynchronously fetch the Astuces
        service.listastuce().enqueue(object : Callback<List<Astuce>> {
            override fun onResponse(call: Call<List<Astuce>>, response: Response<List<Astuce>>) {
                if (response.isSuccessful) {
                    response.body()?.let { astuce ->
                        astuceAdapter.setAstuce(astuce)
                    }
                } else {
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Astuce>>, t: Throwable) {
                showError("Network Error: ${t.message}")
            }
        })
    }



    private fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
}