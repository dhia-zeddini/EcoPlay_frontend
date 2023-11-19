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
import androidx.fragment.app.viewModels
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
import com.example.ecoplay_front.viewModel.ProfileViewModel
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
    private val viewModel by viewModels<ProfileViewModel>()

    // var validBio:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

       viewModel.getProfile(token ?: "")

       viewModel.userProfile.observe(viewLifecycleOwner) { userModel ->
           // Update UI with user profile data
           firstName.setText(userModel.firstName)
           lastName.setText(userModel.lastName)
           email.setText(userModel.email)
           phone.setText(userModel.phoneNumber)
       }


        saveBtn.setOnClickListener {
            val requestModel = RegisterRequestModel(
                firstName.text.toString(),
                lastName.text.toString(),
                email.text.toString(),
                phone.text.toString(),
                "",
                ""
            )
            token?.let { viewModel.updateProfile(it, requestModel) }
        }
        viewModel.updateResponse.observe(viewLifecycleOwner) { updateResponse ->
            // Handle update response
            Snackbar.make(view, "Account updated successfully", Snackbar.LENGTH_SHORT).show()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            // Show error message
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Account")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")

            builder.setPositiveButton("Delete") { dialog, _ ->
                dialog.dismiss()
                token?.let { viewModel.deleteAccount(it) }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

        viewModel.deleteResponse.observe(viewLifecycleOwner) { updateResponse ->
            clearSharedPreferences()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->

            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
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