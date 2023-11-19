package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.viewModel.ForgetPwdViewModel
import com.google.android.material.snackbar.Snackbar

class ForgetPwdActivity : AppCompatActivity() {
    private val viewModel by viewModels<ForgetPwdViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd)

        val emailInput: EditText =findViewById(R.id.etOtp4)




        val back: TextView =findViewById(R.id.tvBack)
        val btnSend: TextView =findViewById(R.id.btnSend)

       btnSend.setOnClickListener {
            viewModel.forgetPwd(emailInput.text.toString())
        }

        viewModel.responseLiveData.observe(this) { response ->
            // Handle success, navigate to OTP activity
            val intent = Intent(applicationContext, OtpActivity::class.java)
            intent.putExtra("token", response.token)
            startActivity(intent)
            finish()
        }

        viewModel.errorMessage.observe(this) { message ->

            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }


        back.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()

        }



    }
}