package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.ecoplay_front.MainActivity
import com.example.ecoplay_front.R
import com.example.ecoplay_front.databinding.ActivityLoginBinding
import com.example.ecoplay_front.viewModel.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Arrays


const val PREF_FILE = "ECOPLAY_PREF"
const val EMAIL = "EMAIL"
const val TOKEN = "TOKEN"
const val LOGGED = "LOGGED"
const val PASSWORD = "PASSWORD"

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var callbackManager:CallbackManager
    lateinit var facebookLogin:LoginButton
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(LayoutInflater.from(this))

        setContentView(binding.root)

        var emailInput:EditText=findViewById(R.id.etOtp4)
        var pwdInput:EditText=findViewById(R.id.etPwd)
        var btnLogin:Button=findViewById(R.id.btnSend)


        var goToRegister:TextView=findViewById(R.id.tvBack)
        var goToForgetPwd:TextView=findViewById(R.id.tvForgotPassword)

        val mSharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE)
        if (mSharedPreferences.getBoolean(LOGGED,false)) {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }
       /* if (mSharedPreferences.getBoolean(LOGGED,false)) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }*/
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()



        btnLogin.setOnClickListener {
            val email = emailInput.text.toString()
            val password = pwdInput.text.toString()
            viewModel.login(email, password)
        }

        // Observing LiveData from ViewModel
        viewModel.loginResponse.observe(this, Observer { response ->
            Log.d("RetrofitCall", "Response successful: $response")
            mSharedPreferences.edit().apply{
                putString(TOKEN, response?.token)
                putBoolean(LOGGED,true)

            }.apply()
            startActivity(Intent(applicationContext, HomePageActivity::class.java))
            finish()
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        })

        goToRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
            finish()

        }

        goToForgetPwd.setOnClickListener {
            startActivity(Intent(applicationContext, ForgetPwdActivity::class.java))
            finish()

        }

        callbackManager = CallbackManager.Factory.create();




        facebookLogin = findViewById(R.id.facebookLogin_button);
        facebookLogin.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        // ...

        facebookLogin.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("FACEBOOK", "facebook token: " + loginResult.accessToken.token)

                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken,
                    GraphRequest.GraphJSONObjectCallback { obj, response ->
                        Log.d("FACEBOOK", "facebook name: " + obj?.getString("name"))
                    })

                val parameters = Bundle()
                parameters.putString("fields", "id,name,link")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                Log.d("FACEBOOK", "facebook onCancel ")
            }

            override fun onError(exception: FacebookException) {
                Log.d("FACEBOOK", "facebook onError: " + exception.message)
            }
        })




    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}


