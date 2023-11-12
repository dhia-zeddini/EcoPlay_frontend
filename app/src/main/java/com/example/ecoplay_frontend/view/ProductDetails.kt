package com.example.ecoplay_frontend.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.ecoplay_frontend.ApiService.AddToCartRequest
import com.example.ecoplay_frontend.ApiService.AddToCartResponse
import com.example.ecoplay_frontend.R
import com.example.ecoplay_frontend.ApiService.CartService
import com.example.ecoplay_frontend.model.ProductModel
import retrofit2.Call
import java.io.Serializable

import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductDetails : AppCompatActivity() {
    private lateinit var buttonSubmit: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)




        val product = intent.getSerializableExtra("product") as? ProductModel
        Log.d("MainActivity", "Received product: $product")
        val imageView: ImageView = findViewById(R.id.imageView)
        val tvNameP: TextView =findViewById(R.id.tvNameP)
        val tvDescriptionP: TextView =findViewById(R.id.textView3)
        val tvPriceP: TextView =findViewById(R.id.textView2)



        tvNameP.text=product?.nameP
        tvDescriptionP.text=product?.descriptionP
        tvPriceP.text=product?.priceP.toString()
        Glide.with(this)
            .load("http://192.168.99.207:8088/images/product/${product?.image}")
            .into(imageView)





        //////////////////////////////////////////


        buttonSubmit = findViewById(R.id.textViewnav)
        buttonSubmit.setOnClickListener {
            val productId = "654b8a14c0ba90f4e6d36007"
            addToCart(productId)
            startActivity(Intent(this, ViewCart::class.java))
            }
        }



    /// fuunctions
    private fun addToCart(productId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.99.207:8088/") // URL for your API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(CartService::class.java)
        val cartId = "654b7efb61e0a843c747f81d" //  cartId

        // Create the request body
        val request = AddToCartRequest(cartId, productId)

        // Enqueue the call to asynchronously execute the PUT request
        service.addToCart(request).enqueue(object : Callback<AddToCartResponse> {
            override fun onResponse(
                call: Call<AddToCartResponse>,
                response: Response<AddToCartResponse>
            ) {
                if (response.isSuccessful) {
                    // Handle the successful response here
                    Log.d("RetrofitResponse", "Product added: ${response.body()?.message}")
                } else {
                }
            }
            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
            }
        })
    }

}
