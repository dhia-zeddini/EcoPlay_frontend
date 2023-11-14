package com.example.ecoplay_front.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.view.PREF_FILE
import com.example.ecoplay_front.view.ProfileActivity
import com.example.ecoplay_front.view.TOKEN
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val privacyBtn: Button? = view.findViewById(R.id.privacyBtn)
        val firstName: EditText = view.findViewById(R.id.firstName)
        val lastName: EditText = view.findViewById(R.id.lastName)
        val email: EditText = view.findViewById(R.id.email)
        val phone: EditText = view.findViewById(R.id.phoneNumber)
        val privacyLayout: LinearLayout? = view.findViewById(R.id.privacyLayout)
        val suffixIconUp = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
        val suffixIconDown = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)

        val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        var token:String?=mSharedPreferences.getString(TOKEN,"no token")
        Log.d("RetrofitCall","prefs${token}")

        val BASE_URL = "http://192.168.1.116:9001/"

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(UserService::class.java)

        val call = apiService.profile("Bearer ${token}")
        call.enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    Log.d("RetrofitCall", "Profile Response successful: ${response.code()}")
                    firstName.setText(response.body()?.firstName )
                    lastName.setText(response.body()?.lastName)
                    email.setText(response.body()?.email)
                    phone.setText(response.body()?.phoneNumber)


                } else if (response.code()==404){
                    Snackbar.make(
                        view.findViewById(android.R.id.content),
                        "User does not exist ",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    // Log error with response code
                    Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                }

                Log.d("RetrofitCall", " Response code: ${response.code()}")
                Log.d("RetrofitCall", "Response body: ${response.body()}")
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                // Log error throwable
                Log.d("RetrofitCall", "Call failed with error", t)

                Snackbar.make(
                    view.findViewById(android.R.id.content),
                    "server error",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
        privacyBtn?.setOnClickListener {
            if (privacyLayout?.isVisible == true) {
                privacyLayout.visibility = View.GONE
                privacyBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconDown, null)
            } else {
                privacyLayout?.visibility = View.VISIBLE
                privacyBtn?.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconUp, null)
            }
        }

        return view
    }
}