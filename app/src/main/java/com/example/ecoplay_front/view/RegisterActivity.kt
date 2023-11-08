package com.example.ecoplay_front.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.databinding.ActivityRegisterBinding
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding=ActivityRegisterBinding.inflate(LayoutInflater.from(this))


        var emailInput: EditText =findViewById(R.id.etEmail)
        var firstNameInput: EditText =findViewById(R.id.etFirstName)
        var lastNameInput: EditText =findViewById(R.id.etLastName)
        var phoneInput: EditText =findViewById(R.id.etPhoneNumber)
        var pwdInput: EditText =findViewById(R.id.etPassword)
        var confirmPwdInput: EditText =findViewById(R.id.etConfirmPwd)
        var btnRegister: Button =findViewById(R.id.btnSend)





        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val BASE_URL = "http://192.168.1.116:9001/" // Remplacez cette URL par votre propre URL

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(UserService::class.java)

        btnRegister.setOnClickListener {

            if (pwdInput.text.toString() == confirmPwdInput.text.toString()){
            val registerRequestModel = RegisterRequestModel(
                firstNameInput.text.toString(),
                lastNameInput.text.toString(),
                emailInput.text.toString(),
                phoneInput.text.toString(),
                pwdInput.text.toString(),
                ""
            )
                //Log.d("RetrofitCall", "Response successful: ${registerRequestModel}")
            val call = apiService.register(registerRequestModel)
            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {

                        Log.d("RetrofitCall", "Response successful: ${response.code()}")

                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()


                    } else if (response.code() == 403) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Account already exist ",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                    }

                    Log.d("RetrofitCall", "Response body: ${response.body()}")
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // Log error throwable
                    Log.d("RetrofitCall", "Call failed with error", t)

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "server error",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            })
        }else{
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "confirm your password",
                    Snackbar.LENGTH_SHORT
                ).show()

            }

        }


    }
}