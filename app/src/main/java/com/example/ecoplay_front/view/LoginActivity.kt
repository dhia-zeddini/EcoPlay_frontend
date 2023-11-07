package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.apiTest
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.repository.UserRepository
import com.example.ecoplay_front.viewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var emailInput:TextView=findViewById(R.id.etEmail)
        var pwdInput:TextView=findViewById(R.id.etPwd)
        var btnLogin:Button=findViewById(R.id.btnLogin)
        emailInput.text = "userNames"
       // userViewModel = UserViewModel(UserRepository())
/*
        userViewModel.getUsers { users ->
            if (users != null) {
                // Update your UI with the list of users
                val userNames = users.joinToString("\n") { it.email }
                //emailInput.text = userNames
            } else {
                // Handle error or show a message in case of an unsuccessful API call
            }
        }*/
btnLogin.setOnClickListener {
    val userInterface= apiTest.create()
    userInterface.login(UserModel(emailInput.text.toString(),pwdInput.text.toString()))
        .enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful){
                    Log.d("Retrofit", "Request succeeded")

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        " jawek behi",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }

            }
            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "error server",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }

        })
}

    }
}