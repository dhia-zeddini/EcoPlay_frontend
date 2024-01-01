package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.CartService
import com.example.ecoplay_front.apiService.RetrofitInstance
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.adapter.CartAdapter
import com.example.ecoplay_front.model.CalculateCartTotalRequest
import com.example.ecoplay_front.model.CalculateCartTotalResponse
import com.example.ecoplay_front.model.CartIdRequest
import com.example.ecoplay_front.model.CreatePaymentIntentRequest
import com.example.ecoplay_front.model.PaymentIntentResponse
import com.example.ecoplay_front.model.RemoveProductRequest
import com.example.ecoplay_front.model.ResponseMessage
import com.google.android.material.snackbar.Snackbar
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.PaymentSheetResultCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewCart : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var makePaymentImageView: ImageView
    //////
    private lateinit var totalAmountTextView: TextView
    private lateinit var paymentSheet: PaymentSheet
    //// PROGRESS BAR
    private lateinit var progressBar: ProgressBar
    private lateinit var token:String

    private val paymentSheetResultCallback = PaymentSheetResultCallback { result ->
        when (result) {
            is PaymentSheetResult.Completed -> {
                // Handle the payment success
                Log.d("Payment", "Payment completed successfully.")
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show()

            }
            is PaymentSheetResult.Canceled -> {
                // Handle the cancellation
                Log.d("Payment", "Payment canceled.")
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_LONG).show()
            }
            is PaymentSheetResult.Failed -> {
                Log.e("Payment", "Payment failed.", result.error)
                Toast.makeText(this, "Payment failed: ${result.error}", Toast.LENGTH_LONG).show()

            }
        }
    }

    private var totalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)
        val backhImageView: ImageView = findViewById(R.id.backh)

        backhImageView.setOnClickListener {
            // Add your intent code here to start the desired activity
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
        }

        val mSharedPreferences = applicationContext.getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
         token= mSharedPreferences.getString(TOKEN,"no token").toString()
        /////////////// PROGRESS BAR //////////
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        //////// PROGRESS BAR //////////







        paymentSheet = PaymentSheet(this, paymentSheetResultCallback)
        ///////: Publishable Key Stripe ///////
        PaymentConfiguration.init(
            applicationContext,
            "pk_test_51OCiyzA6xn7XOwxetYBolugYgUT1Y2RT5pudqJ4DS7702ZYQ6oroeyQu403hnZaUOBbuSwz5Jt46kZoqn6jtFNVB006LUPWwm3" // Replace with your actual publishable key
        )
        ///////: Publishable Key Stripe ///////
        makePaymentImageView = findViewById(R.id.imageViewpay) // Replace with your actual ImageView ID
        makePaymentImageView.setOnClickListener {
            Log.d("ViewCart", "Make Payment clicked")
            startStripeCheckout()
        }



        recyclerView = findViewById(R.id.challenge_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CartAdapter()
        recyclerView.adapter = cartAdapter


        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Move not supported
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val productId = cartAdapter.getProductIdAtPosition(position)
                Log.d("SwipeDebug", "Swiped position: $position")
                Log.d("SwipeDebug", "Product ID to delete: $productId")
                deleteProductFromCart(productId)
                // Comment out the line below if you are updating the list in the deleteProductFromCart response
                cartAdapter.notifyItemRemoved(position)
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

///////////// calculate amount ///////////////////////
        totalAmountTextView = findViewById(R.id.totalamount)


//////////////////////////////////////////////////////////////////////////////////////:


        loadCartItems()
        calculateCartTotal()

    }

    private fun loadCartItems() {
        val service = RetrofitInstance.retrofit.create(CartService::class.java)
        val cartId = "655d12fb3ebfe227d849215c"

        service.getProductsInCart("Bearer $token",CartIdRequest(cartId)).enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {
                if (response.isSuccessful) {
                    response.body()?.let { products ->
                        cartAdapter.setCartList(products)
                        calculateCartTotal()
                        Log.d("RetrofitResponse", "Products loaded: $products")

                        progressBar.visibility = View.GONE

                    }
                    loadCartItems()
                } else {
                    Log.e("RetrofitResponse", "Error loading cart items: ${response.code()}")
                    showError("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                showError("Network Error: ${t.message}")
                Log.e("RetrofitResponse", "Network Error: ${t.message}")
            }
        })
    }



    ////////////////////////////// calculate sum ///////////////////
    private fun calculateCartTotal() {
        val cartId = "655d12fb3ebfe227d849215c"
        val service = RetrofitInstance.retrofit.create(CartService::class.java)
        val request = CalculateCartTotalRequest(cartId)

        service.calculateCartTotal("Bearer $token",request).enqueue(object : Callback<CalculateCartTotalResponse> {
            override fun onResponse(
                call: Call<CalculateCartTotalResponse>,
                response: Response<CalculateCartTotalResponse>
            ) {
                if (response.isSuccessful) {
                    val totalAmount = response.body()?.total ?: 0.0
                    updateUITotalAmount(totalAmount)
                    Log.d("RetrofitResponse", "Total calculated: $totalAmount")
                    //   startStripeCheckout()
                } else {
                    Log.e(
                        "RetrofitError",
                        "Error calculating total: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<CalculateCartTotalResponse>, t: Throwable) {
                Log.e("RetrofitError", "Call failed", t)
            }
        })


    }
    private fun updateUITotalAmount(totalAmount: Double) {
        this.totalAmount = totalAmount
        Log.d("UIUpdate", "Updating total amount on the UI thread: ${Thread.currentThread().name}")
        Log.d("UIUpdate", "Total Amount to Update UI: $totalAmount")
        totalAmountTextView.text = String.format("Total Amount: %.2f cents", totalAmount)
    }

    private fun showError(message: String) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show()
    }

    private fun deleteProductFromCart(productId: String) {
        val cartId = "655d12fb3ebfe227d849215c"
        val removeProductRequest = RemoveProductRequest(cartId = cartId, productId = productId)
        val service = RetrofitInstance.retrofit.create(CartService::class.java)
        service.deleteProductFromCart("Bearer $token",removeProductRequest)
            .enqueue(object : Callback<ResponseMessage> {
                override fun onResponse(
                    call: Call<ResponseMessage>,
                    response: Response<ResponseMessage>
                ) {
                    if (response.isSuccessful) {
                        Log.d("DeleteDebug", "Product deletion successful")

                    } else {
                        Log.e(
                            "DeleteDebug",
                            "Failed to delete product: ${response.errorBody()?.string()}"
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseMessage>, t: Throwable) {
                    Log.e("DeleteDebug", "Network error or exception during API call", t)
                }
            })
    }



    ////////////////// STRIPE ////////////////////////
    private fun startStripeCheckout() {
        val service = RetrofitInstance.retrofit.create(CartService::class.java)
        service.createPaymentIntent(CreatePaymentIntentRequest(totalC = totalAmount)).enqueue(object : Callback<PaymentIntentResponse> {
            override fun onResponse(
                call: Call<PaymentIntentResponse>,
                response: Response<PaymentIntentResponse>
            ) {
                if (response.isSuccessful) {
                    val clientSecret = response.body()?.clientSecret
                    Log.d("ViewCart", "Received client secret: $clientSecret")

                    if (clientSecret != null) {
                        presentStripePaymentSheet(clientSecret)
                    }else {
                        Log.d("ViewCart", "Received client secret: $clientSecret")
                    }
                } else {
                    Log.e("StripePayment", "Error creating payment intent: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaymentIntentResponse>, t: Throwable) {
                Log.e("StripePayment", "Network error creating payment intent", t)
            }
        })
    }

    private fun presentStripePaymentSheet(paymentIntentClientSecret: String) {
        val configuration = PaymentSheet.Configuration("Your Business Name") // Replace with your actual business name
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration)
    }
    ///////////// PROGRESS BAR ///////////


}


