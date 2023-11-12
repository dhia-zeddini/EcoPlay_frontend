package com.example.ecoplay_frontend.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_frontend.R
import com.example.ecoplay_frontend.ApiService.CalculateCartTotalRequest
import com.example.ecoplay_frontend.ApiService.CalculateCartTotalResponse
import com.example.ecoplay_frontend.ApiService.CartIdRequest
import com.example.ecoplay_frontend.ApiService.CartService
import com.example.ecoplay_frontend.model.ProductModel
import com.example.ecoplay_frontend.viewModel.CartAdapter
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewCart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter


//////:
    private lateinit var totalAmountTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)


/////////////
        totalAmountTextView = findViewById(R.id.totalamount)


//////////////////////////////////////////////////////////////////////////////////////:
        recyclerView = findViewById(R.id.challenge_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter

        loadCartItems()
    }

    private fun loadCartItems() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.99.207:8088/") // Make sure this is the correct base URL for your API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        ////////////////

        val service = retrofit.create(CartService::class.java)
        val cartId = "654b7efb61e0a843c747f81d"

        service.getProductsInCart(CartIdRequest(cartId)).enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        cartAdapter.setCartList(products)
                        Log.d("RetrofitResponse", "Response data: $products")
                    }
                    Log.d("RetrofitResponse", "Response code: ${response.code()}")
                } else {
                    Log.e("RetrofitResponse", "Error: ${response.code()}")
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                showError("Network Error: ${t.message}")
            }
        })
    }



        //////////////////////////////
        private fun calculateCartTotal() {
            val cartId = "654b7efb61e0a843c747f81d"

            // Assuming you have a cartId variable somewhere
            val retrofit = Retrofit.Builder()
                .baseUrl("http:192.168.99.207:8088/") // Use your actual base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val service = retrofit.create(CartService::class.java)

            val request = CalculateCartTotalRequest(cartId)

            service.calculateCartTotal(request).enqueue(object : Callback<CalculateCartTotalResponse> {
                override fun onResponse(
                    call: Call<CalculateCartTotalResponse>,
                    response: Response<CalculateCartTotalResponse>
                ) {
                    if (response.isSuccessful) {
                        // Success - the cart total is calculated
                        val totalAmount = response.body()?.total ?: 0.0
                        // Update your UI with the total amount
                        Log.d("RetrofitResponse", "Total calculated: $totalAmount")
                    } else {
                        // Handle the case where the server responds with an error
                        Log.e("RetrofitError", "Error calculating total: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<CalculateCartTotalResponse>, t: Throwable) {
                    // Handle the error (e.g., no internet connection)
                    Log.e("RetrofitError", "Call failed", t)
                }
            })
        }








    private fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }
}

// Retrofit interface definition

