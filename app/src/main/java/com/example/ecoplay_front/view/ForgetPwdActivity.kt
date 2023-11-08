package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import com.example.ecoplay_front.MainActivity
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.LoginRequestModel
import com.example.ecoplay_front.model.LoginRespenseModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForgetPwdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd)

        var emailInput: EditText =findViewById(R.id.etEmail)




        var back: TextView =findViewById(R.id.tvBack)
        var btnSend: TextView =findViewById(R.id.btnSend)


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

        val apiService = retrofit.create(UserService::class.java)

        btnSend.setOnClickListener {
            Log.d("RetrofitCall", "Response successful: ${emailInput.text.toString()}")
            val forgetRequestModel = LoginRequestModel(emailInput.text.toString(), "") // Assurez-vous d'avoir les valeurs appropriées pour phoneNumber et password

            val call = apiService.forgetPwd(forgetRequestModel)
            call.enqueue(object : Callback<LoginRespenseModel> {
                override fun onResponse(call: Call<LoginRespenseModel>, response: Response<LoginRespenseModel>) {
                    if (response.isSuccessful) {
                        // Log success message with response code
                        Log.d("RetrofitCall", "Response successful: ${response.code()}")

                        startActivity(Intent(applicationContext, OtpActivity::class.java))
                        finish()

                    } else if (response.code()==404){
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "User does not exist ",
                            Snackbar.LENGTH_SHORT
                        ).show()

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
                        "server error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })


        }

        back.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()

        }



    }
}