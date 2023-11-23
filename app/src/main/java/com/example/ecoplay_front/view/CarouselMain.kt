package com.example.ecoplay_front.view
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_front.adapter.CarouselAdapter
import com.example.ecoplay_front.viewModel.CarouselViewModel
import androidx.activity.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.uttil.SpaceItemDecoration

class CarouselMain : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var carouselAdapter: CarouselAdapter
    private val viewModel: CarouselViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carousel_main)
        setupRecyclerView()
        setupCartTextView()
        observeProducts()
        viewModel.fetchProductsFromBackend()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewCarousel)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.carousel_item_spacing)
        recyclerView.addItemDecoration(SpaceItemDecoration(spacingInPixels))
        carouselAdapter = CarouselAdapter(mutableListOf())
        recyclerView.adapter = carouselAdapter
    }

    private fun setupCartTextView() {
        val cartTextView: TextView = findViewById(R.id.cart)
        cartTextView.setOnClickListener {
            startActivity(Intent(this, ViewCart::class.java))
        }
    }

    private fun observeProducts() {
        viewModel.products.observe(this, Observer { products ->
            carouselAdapter.updateData(products)
            Log.d("background fetch","mrigl ${products}")
        })
    }
}
