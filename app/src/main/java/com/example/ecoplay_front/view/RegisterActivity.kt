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
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.databinding.ActivityRegisterBinding
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.viewModel.RegisterViewModel
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
    private val viewModel by viewModels<RegisterViewModel>()

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


       btnRegister.setOnClickListener {
            if (pwdInput.text==confirmPwdInput){
                val registerRequestModel = RegisterRequestModel(
                    firstNameInput.text.toString(),
                    lastNameInput.text.toString(),
                    emailInput.text.toString(),
                    phoneInput.text.toString(),
                    pwdInput.text.toString(),
                    "" // or handle the image URI
                )

                viewModel.register(registerRequestModel)
            }else{
                Snackbar.make(findViewById(android.R.id.content), "You have to confirm your Password", Snackbar.LENGTH_SHORT).show()

            }

        }

        viewModel.registerResponse.observe(this, { response ->
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
        })

        viewModel.errorMessage.observe(this, { message ->
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        })



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




