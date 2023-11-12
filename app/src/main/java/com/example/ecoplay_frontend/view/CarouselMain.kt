// CarouselMain.kt
package com.example.ecoplay_frontend.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_frontend.R
import com.example.ecoplay_frontend.ApiService.RetrofitInstance
import com.example.ecoplay_frontend.viewModel.CarouselAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarouselMain : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel_main)

        recyclerView = findViewById(R.id.recyclerViewCarousel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Initialize the adapter with an empty list
        carouselAdapter = CarouselAdapter(mutableListOf())
        recyclerView.adapter = carouselAdapter

        // Fetch the products from the backend
        fetchProductsFromBackend()
    }

    private fun fetchProductsFromBackend() {
        lifecycleScope.launch {
            try {
                // Move the network call to an IO Dispatcher context
                val productList = withContext(Dispatchers.IO) {
                    RetrofitInstance.apiService.getAllProducts()
                }
                // Update the adapter with the retrieved product list
                carouselAdapter.updateData(productList)
                Log.d("RETROFIT", "list$productList")
            } catch (e: Exception) {
                // Log the exception or show an error message
            }
        }
    }

}
