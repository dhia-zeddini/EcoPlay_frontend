package com.example.ecoplay_front.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import com.example.ecoplay_front.R
import com.example.ecoplay_front.viewModel.ResetPwdViewModel
import com.google.android.material.snackbar.Snackbar

class ResetPwdActivity : AppCompatActivity() {

    private val viewModel by viewModels<ResetPwdViewModel>()
    private var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pwd)


        token = intent.getStringExtra("token")
        val newPwd: EditText = findViewById(R.id.etNewPwd)
        val confirmPwd: EditText = findViewById(R.id.etConfirmPwd)

        val btnConfirm: TextView = findViewById(R.id.btnConfirm)


        btnConfirm.setOnClickListener {

            if (newPwd.text.toString() == confirmPwd.text.toString()) {
                token?.let {
                    viewModel.newPwd(it, newPwd.text.toString())
                }


            }

            viewModel.responseLiveData.observe(this) { response ->

                clearSharedPreferences()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }

            viewModel.errorMessage.observe(this) { message ->
                // Show error message
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }
    private fun clearSharedPreferences() {
        // Clear the shared preferences
        val mSharedPreferences = applicationContext.getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        val editor = mSharedPreferences.edit()
        editor.remove(TOKEN)
        editor.remove(LOGGED)
        editor.apply()
    }
}