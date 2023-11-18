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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

        val mSharedPreferences = getSharedPreferences(PREF_FILE, MODE_PRIVATE)



        val BASE_URL = "http://192.168.1.116:9001/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(UserService::class.java)



        btnLogin.setOnClickListener {
            var  email:String=emailInput.text.toString()
            var  password:String=pwdInput.text.toString()
            Log.d("RetrofitCall", "Response successful: ${email}")
            val loginRequestModel = LoginRequestModel(email, password) // Assurez-vous d'avoir les valeurs appropriées pour phoneNumber et password

            val call = apiService.login(loginRequestModel)
            call.enqueue(object : Callback<LoginResponseModel> {
                override fun onResponse(call: Call<LoginResponseModel>, response: Response<LoginResponseModel>) {
                    if (response.isSuccessful) {
                        // Log success message with response code
                        Log.d("RetrofitCall", "Response successful: ${response.code()}")
                        mSharedPreferences.edit().apply{
                            putString(TOKEN, response.body()?.token)
                            putBoolean(LOGGED,true)

                        }.apply()
                        startActivity(Intent(applicationContext, ProfileActivity::class.java))
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


