package com.example.ecoplay_front.fragments

import android.content.Intent
import android.graphics.drawable.Drawable

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.ecoplay_front.R
import com.example.ecoplay_front.apiService.UserService
import com.example.ecoplay_front.model.LoginResponseModel
import com.example.ecoplay_front.model.RegisterRequestModel
import com.example.ecoplay_front.model.RegisterResponse
import com.example.ecoplay_front.model.UpdateResponseModel
import com.example.ecoplay_front.model.UserModel
import com.example.ecoplay_front.view.LOGGED
import com.example.ecoplay_front.view.LoginActivity
import com.example.ecoplay_front.view.OtpActivity
import com.example.ecoplay_front.view.PREF_FILE
import com.example.ecoplay_front.view.ProfileActivity
import com.example.ecoplay_front.view.TOKEN
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor

class ProfileFragment : Fragment() {
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo:PromptInfo
    private lateinit var privacyLayout: LinearLayout
    private lateinit var privacyBtn: Button
    private lateinit var suffixIconUp: Drawable
    private lateinit var suffixIconDown: Drawable
   // var validBio:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

         privacyBtn = view.findViewById(R.id.privacyBtn)
        val saveBtn: Button = view.findViewById(R.id.saveBtn)
        val firstName: EditText = view.findViewById(R.id.firstName)
        val lastName: EditText = view.findViewById(R.id.lastName)
        val email: EditText = view.findViewById(R.id.email)
        val phone: EditText = view.findViewById(R.id.phoneNumber)
       val deleteBtn:TextView=view.findViewById(R.id.deleteBtn)
       val logoutBtn:TextView=view.findViewById(R.id.logoutBtn)
       privacyLayout = view.findViewById(R.id.privacyLayout)
       suffixIconUp = requireContext().getDrawable(R.drawable.arrow_up)!!
       suffixIconDown = requireContext().getDrawable(R.drawable.arrow_down)!!


       val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        var token:String?=mSharedPreferences.getString(TOKEN,"no token")
        Log.d("RetrofitCall","prefs${token}")

        val BASE_URL = "http://192.168.1.4:9002/"

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


        saveBtn.setOnClickListener {

            val registerRequestModel = RegisterRequestModel(
                firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                phone.text.toString(),
                "",
                ""
            )
            Log.d("RetrofitCall", "Response update body: ${registerRequestModel}")
                val call = apiService.updateProfile("Bearer ${token}",registerRequestModel)
                call.enqueue(object : Callback<UpdateResponseModel> {
                    override fun onResponse(
                        call: Call<UpdateResponseModel>,
                        response: Response<UpdateResponseModel>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("RetrofitCall", " update successful: ")

                           Snackbar.make(requireContext(),view.findViewById(R.id.profileFragment),
                                "Account updated successfully",
                                Snackbar.LENGTH_SHORT

                            ).show()
                            Log.d("RetrofitCall", "Response update successful: ${response.code()}")
                        } else if (response.code() == 403) {
                            Snackbar.make(
                                view.findViewById(android.R.id.content),
                                "You are not authorized ",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                        }

                        Log.d("RetrofitCall", "Response update body: ${response.body()}")
                    }

                    override fun onFailure(call: Call<UpdateResponseModel>, t: Throwable) {
                        // Log error throwable
                        Log.d("RetrofitCall", "Call failed with error", t)

                        Snackbar.make(
                            view.findViewById(android.R.id.content),
                            "server error",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })


        }


       deleteBtn.setOnClickListener{
               val builder = AlertDialog.Builder(requireContext())
               builder.setTitle("Delete Account")
                   .setMessage("Are you sure you want to delete your account? This action cannot be undone.")


               builder.setPositiveButton("Delete") { dialog, _ ->
                   dialog.dismiss()
                   val call = apiService.deleteAccount("Bearer $token")
                   call.enqueue(object : Callback<UpdateResponseModel> {
                       override fun onResponse(call: Call<UpdateResponseModel>, response: Response<UpdateResponseModel>) {
                           if (response.isSuccessful) {
                               Log.d("RetrofitCall", "Profile Response successful: ${response.code()}")
                               clearSharedPreferences()
                               val intent=Intent(requireContext(), LoginActivity::class.java)
                               startActivity(intent)



                           } else if (response.code()==403){
                               Snackbar.make(
                                   view.findViewById(android.R.id.content),
                                   "You are not authorized ",
                                   Snackbar.LENGTH_SHORT
                               ).show()
                               // Log error with response code
                               Log.d("RetrofitCall", "Response not successful: ${response.code()}")
                           }

                           Log.d("RetrofitCall", " Response code: ${response.code()}")
                           Log.d("RetrofitCall", "Response body: ${response.body()}")
                       }

                       override fun onFailure(call: Call<UpdateResponseModel>, t: Throwable) {
                           // Log error throwable
                           Log.d("RetrofitCall", "Call failed with error", t)

                           Snackbar.make(
                               view.findViewById(android.R.id.content),
                               "server error",
                               Snackbar.LENGTH_SHORT
                           ).show()
                       }
                   })
               }

               builder.setNegativeButton("Cancel") { dialog, _ ->

                   dialog.dismiss()
               }


               val alertDialog: AlertDialog = builder.create()
               alertDialog.show()
           }

       logoutBtn.setOnClickListener{
           val builder = AlertDialog.Builder(requireContext())
           builder.setTitle("Logout")
               .setMessage("Are you sure you want to logout?")


           builder.setPositiveButton("Ok") { dialog, _ ->
               dialog.dismiss()

                           clearSharedPreferences()
                           val intent=Intent(requireContext(), LoginActivity::class.java)
                           startActivity(intent)
           }

           builder.setNegativeButton("Cancel") { dialog, _ ->

               dialog.dismiss()
           }


           val alertDialog: AlertDialog = builder.create()
           alertDialog.show()
       }

        privacyBtn.setOnClickListener {
            Log.d("BIOMETRIC"," bioooo yess")
            if (privacyLayout?.isVisible == true) {

                privacyLayout.visibility = View.GONE
                privacyBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconDown, null)
            } else {
                Log.d("BIOMETRIC"," bioooo ${checkDeviceHasBiometric()}")
                checkDeviceHasBiometric()

                    Log.d("BIOMETRIC"," bioooo yess")
                   /* privacyLayout?.visibility = View.VISIBLE
                    privacyBtn?.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconUp, null)*/



            }
        }



        return view
    }

    private fun createBiometricListener(){
        executor=ContextCompat.getMainExecutor(requireContext())
        biometricPrompt= BiometricPrompt(requireActivity(),executor,object :BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BIOMETRIC"," bio $errString")

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BIOMETRIC"," bio failed")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BIOMETRIC"," bio succ")



            }
        })
    }
    private fun onBiometricAuthenticationSucceeded() {
        Log.d("BIOMETRIC", " bio succ")
        // Handle UI updates or other actions upon successful biometric authentication
        requireActivity().runOnUiThread {
            privacyLayout.visibility = View.VISIBLE
            privacyBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconUp, null)
        }
    }
    private fun createPromptInfo(){
        promptInfo=BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric for EcoPlay")
            .setSubtitle("biometric credential")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)  // Specify fingerprint modality
            .build()
    }
    private fun checkDeviceHasBiometric(){

        val biometricManager=androidx.biometric.BiometricManager.from(requireContext())
        when(biometricManager.canAuthenticate(BIOMETRIC_STRONG)){
            BiometricManager.BIOMETRIC_SUCCESS->{
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
                privacyLayout?.let {
                    it.visibility = View.VISIBLE
                    privacyBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, suffixIconUp, null)
                }

            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE->{
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
                Log.d("BIOMETRIC"," bio BIOMETRIC_ERROR_NO_HARDWARE")

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE->{
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
                Log.d("BIOMETRIC"," bio BIOMETRIC_ERROR_HW_UNAVAILABLE")


            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED->{
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
                Log.d("BIOMETRIC"," bio BIOMETRIC_ERROR_NONE_ENROLLED")

            }
        }


    }
    private fun clearSharedPreferences() {
        // Clear the shared preferences
        val mSharedPreferences = requireContext().getSharedPreferences(PREF_FILE, AppCompatActivity.MODE_PRIVATE)
        val editor = mSharedPreferences.edit()
        editor.remove(TOKEN)
        editor.remove(LOGGED)
        editor.apply()
    }
}