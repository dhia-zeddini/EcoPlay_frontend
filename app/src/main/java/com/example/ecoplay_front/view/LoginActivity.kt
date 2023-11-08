package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.apiTest
import com.example.ecoplay_front.databinding.ActivityLoginBinding
import com.example.ecoplay_front.model.LoginRespenseModel
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.repository.UserRepository
import com.example.ecoplay_front.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import okhttp3.logging.HttpLoggingInterceptor




class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        var emailInput:TextView=findViewById(R.id.etEmail)
        var pwdInput:TextView=findViewById(R.id.etPwd)
        var btnLogin:Button=findViewById(R.id.btnLogin)




        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()





        val BASE_URL = "http://192.168.251.18:9001/" // Remplacez cette URL par votre propre URL

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(apiTest::class.java)

        btnLogin.setOnClickListener {

            val userModel = UserModel(emailInput.text.toString(), pwdInput.text.toString()) // Assurez-vous d'avoir les valeurs appropriées pour phoneNumber et password

            val call = apiService.login(userModel)
            call.enqueue(object : Callback<LoginRespenseModel> {
                override fun onResponse(call: Call<LoginRespenseModel>, response: Response<LoginRespenseModel>) {
                    if (response.isSuccessful) {
                        // Log success message with response code
                        Log.d("RetrofitCall", "Response successful: ${response.code()}")

                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Jawek behi",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        // Log error with response code
                        Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                    }

                    // Optionally log the raw JSON response body
                    Log.d("RetrofitCall", "Response body: ${response.body()}")
                }

                override fun onFailure(call: Call<LoginRespenseModel>, t: Throwable) {
                    // Log error throwable
                    Log.d("RetrofitCall", "Call failed with error", t)

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Leee",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

    }
}

