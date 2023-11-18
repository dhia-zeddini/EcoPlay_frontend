package com.example.ecoplay_front.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.databinding.ActivityRegisterBinding
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var selectedImageUri: Uri? = null
    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var emailInput = binding.etOtp4
        var firstNameInput = binding.etFirstName
        var lastNameInput = binding.etLastName
        var phoneInput = binding.etPhoneNumber
        var pwdInput = binding.etPassword
        var confirmPwdInput = binding.etConfirmPwd
        var btnRegister = binding.btnSend
        var imageButton = binding.imageButton


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

            if (pwdInput.text.toString() == confirmPwdInput.text.toString()) {
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
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "confirm your password",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        imageButton.setOnClickListener {
            pickImageFromGallery()
        }

    }



    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("UPLOADIMAGE","image $resultCode")
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            loadSelectedImageIntoImageView(selectedImageUri)

            Log.d("taswyra","image $selectedImageUri")

        }
    }


    private fun loadSelectedImageIntoImageView(imageUri: Uri?) {
        if (imageUri != null) {
            Log.d("image ya zebla", "Image URI: $imageUri")
            try {
                Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.imageButton)
            } catch (e: Exception) {
                Log.e("GlideError", "Error loading image with Glide: ${e.message}")
                e.printStackTrace()
            }
        }
    }





}




