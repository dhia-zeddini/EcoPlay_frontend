package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.viewModel.OtpViewModel
import com.google.android.material.snackbar.Snackbar


class OtpActivity : AppCompatActivity() {
    private val viewModel by viewModels<OtpViewModel>()
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        token = intent.getStringExtra("token")
        Log.d("RetrofitCall", "Response token: $token")

        val etOtp1: EditText =findViewById(R.id.etOtp1)
        val etOtp2: EditText =findViewById(R.id.etOtp2)
        val etOtp3: EditText =findViewById(R.id.etOtp3)
        val etOtp4: EditText =findViewById(R.id.etOtp4)

        val btnSend: TextView =findViewById(R.id.btnSend)


       btnSend.setOnClickListener {
            val otp = etOtp1.text.toString() + etOtp2.text.toString() +
                    etOtp3.text.toString() + etOtp4.text.toString()

            token?.let { viewModel.verifyOtp(it, otp) }
        }

        viewModel.responseLiveData.observe(this) { response ->
            // Handle success, navigate to ResetPwdActivity
            val intent = Intent(applicationContext, ResetPwdActivity::class.java)
            intent.putExtra("token", response.token)
            startActivity(intent)
            finish()
        }

        viewModel.errorMessage.observe(this) { message ->
            // Show error message
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }


    }
}