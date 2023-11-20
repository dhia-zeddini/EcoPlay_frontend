package com.example.ecoplay_front.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecoplay_front.databinding.ActivityRegisterBinding
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.viewModel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var selectedImageUri: Uri? = null
    private val viewModel by viewModels<RegisterViewModel>()

    companion object {
        val IMAGE_REQUEST_CODE = 1_000
    }
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val emailInput = binding.etOtp4
        val firstNameInput = binding.etFirstName
        val lastNameInput = binding.etLastName
        val phoneInput = binding.etPhoneNumber
        val pwdInput = binding.etPassword
        val confirmPwdInput = binding.etConfirmPwd
        val btnRegister = binding.btnSend
        val imageButton = binding.imageButton




       btnRegister.setOnClickListener {
            /*if (pwdInput.text.toString()==confirmPwdInput.text.toString()){
                selectedImageUri?.let { it1 ->
                    viewModel.register( firstNameInput.text.toString(),
                        lastNameInput.text.toString(),
                        emailInput.text.toString(),
                        phoneInput.text.toString(),
                        pwdInput.text.toString(),
                        it1
                    )
                }
            }else{
                Snackbar.make(findViewById(android.R.id.content), "You have to confirm your Password", Snackbar.LENGTH_SHORT).show()

            } */
           attemptRegister()


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




    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part? {
        return try {
            contentResolver.openInputStream(fileUri)?.use { inputStream ->
                val requestFile = inputStream.readBytes().toRequestBody("image/jpeg".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("avatar", "file.jpg", requestFile)
            }
        } catch (e: Exception) {
            Log.e("RegisterActivity", "Error preparing file part", e)
            null
        }
    }


    private fun attemptRegister() {
        Log.d("RegisterDebug", "attemptRegister() called")
        val email = binding.etOtp4.text.toString()
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val phone = binding.etPhoneNumber.text.toString()
        val pwd = binding.etPassword.text.toString()
        val confirmPwd = binding.etConfirmPwd.text.toString()

        if (pwd == confirmPwd) {
            val avatarPart = selectedImageUri?.let { prepareFilePart(it) }
            viewModel.register(
                firstName,
                lastName,
                email,
                phone,
                pwd,
                avatarPart
            )
        } else {
            Snackbar.make(findViewById(android.R.id.content), "You have to confirm your Password", Snackbar.LENGTH_SHORT).show()
        }
    }








}




