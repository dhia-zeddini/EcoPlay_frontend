package com.example.ecoplay_front.view

import ProductDetailsViewModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.uttil.Constants

class ProductDetails : AppCompatActivity() {
    private lateinit var buttonSubmit: TextView
    private lateinit var viewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        viewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)

        val product = intent.getSerializableExtra("product") as? ProductModel
        Log.d("MainActivity", "Received product: $product")
        val imageView: ImageView = findViewById(R.id.imageView)
        val tvNameP: TextView = findViewById(R.id.tvNameP)
        val tvDescriptionP: TextView = findViewById(R.id.textView3)
        val tvPriceP: TextView = findViewById(R.id.textView2)

        tvNameP.text = product?.nameP
        tvDescriptionP.text = product?.descriptionP
        tvPriceP.text = product?.priceP.toString()
        Glide.with(this)
            .load("${Constants.BASE_URL}images/product/${product?.image}")
            .into(imageView)

        buttonSubmit = findViewById(R.id.textViewnav)
        buttonSubmit.setOnClickListener {
            val productId = product?._id
            if (productId != null) {
                // Call the ViewModel function to add the product to the cart
                viewModel.addToCart(productId, "654b7efb61e0a843c747f81d") { success ->
                    if (success) {
                        // Product added successfully
                        Toast.makeText(applicationContext, "Product added successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        // Error occurred while adding the product
                        Toast.makeText(applicationContext, " product all ready added to cart", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            startActivity(Intent(this, ViewCart::class.java))
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, CarouselMain::class.java)
        startActivity(intent)
        finish() // Finish the current activity
        super.onBackPressed()
    }
}
