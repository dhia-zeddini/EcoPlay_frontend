package com.example.ecoplay_front.view

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.viewModel.ForgetPwdViewModel
import com.google.android.material.snackbar.Snackbar

class ForgetPwdActivity : AppCompatActivity() {
    private val viewModel by viewModels<ForgetPwdViewModel>()
    lateinit var method:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pwd)

        val emailInput: EditText =findViewById(R.id.etOtp4)
        val emailMethod: TextView =findViewById(R.id.emailMethod)
        val smsMethod: TextView =findViewById(R.id.smsMethod)
         var emailIcon: Drawable = applicationContext.getResources().getDrawable(R.drawable.email_icon);
         var phoneIocn: Drawable = applicationContext.getResources().getDrawable(R.drawable.phone_icon);


        smsMethod.setOnClickListener {
            smsMethod.setBackgroundResource(R.drawable.method_send_shape)
            emailMethod.setBackgroundResource(0)
            //emailInput.setCompoundDrawables(emailIcon,null,null,null)
            method="sms"
        }
        emailMethod.setOnClickListener {
            emailMethod.setBackgroundResource(R.drawable.method_send_shape)
            smsMethod.setBackgroundResource(0)
           // emailInput.setCompoundDrawables(phoneIocn,null,null,null)
            method="email"


        }


        val back: TextView =findViewById(R.id.tvBack)
        val btnSend: TextView =findViewById(R.id.btnSend)

       btnSend.setOnClickListener {
           if(method=="email"){
               viewModel.forgetPwd(emailInput.text.toString())

           }else if(method=="sms")
               viewModel.forgetPwdSms(emailInput.text.toString())
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