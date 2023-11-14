package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ecoplay_front.MainActivity
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.databinding.ActivityLoginBinding
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.model.LoginRequestModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor




class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        var emailInput:EditText=findViewById(R.id.etOtp4)
        var pwdInput:EditText=findViewById(R.id.etPwd)
        var btnLogin:Button=findViewById(R.id.btnSend)


        var goToRegister:TextView=findViewById(R.id.tvBack)
        var goToForgetPwd:TextView=findViewById(R.id.tvForgotPassword)


        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()





        val BASE_URL = "http://172.16.2.167:9001/" // Remplacez cette URL par votre propre URL

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(UserService::class.java)

        btnLogin.setOnClickListener {
            Log.d("RetrofitCall", "Response successful: ${emailInput.text.toString()}")
            val loginRequestModel = LoginRequestModel(emailInput.text.toString(), pwdInput.text.toString()) // Assurez-vous d'avoir les valeurs appropriées pour phoneNumber et password

            val call = apiService.login(loginRequestModel)
            call.enqueue(object : Callback<LoginResponseModel> {
                override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                    if (response.isSuccessful) {
                        // Log success message with response code
                        Log.d("RetrofitCall", "Response successful: ${response.code()}")

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()



                } else if (response.code()==404){
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "User does not exist ",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        // Log error with response code
                        Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                    }else if (response.code()==401){
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Invalid password",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        // Log error with response code
                        Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                    }

                    // Optionally log the raw JSON response body
                    Log.d("RetrofitCall", "Response body: ${response.body()}")
                }

                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    // Log error throwable
                    Log.d("RetrofitCall", "Call failed with error", t)

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "server error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

        goToRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()

        }

        goToForgetPwd.setOnClickListener {
            startActivity(Intent(applicationContext, ForgetPwdActivity::class.java))
            finish()

        }

    }
}

