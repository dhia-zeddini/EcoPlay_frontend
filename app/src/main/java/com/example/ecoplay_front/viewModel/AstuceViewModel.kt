package com.example.ecoplay_front.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.model.Astuce
import com.example.ecoplay_front.repository.AstuceRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AstuceViewModel : ViewModel() {
    private val astuces: MutableLiveData<List<Astuce>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()

    private val repository = AstuceRepository()

    fun getAstuces(): LiveData<List<Astuce>> = astuces
    fun getErrorMessage(): LiveData<String> = errorMessage

    fun loadAstuces() {
        Log.d("AstuceViewModel", "loadAstuces() called")

        repository.listAstuces(object : Callback<List<Astuce>> {
            override fun onResponse(call: Call<List<Astuce>>, response: Response<List<Astuce>>) {
                if (response.isSuccessful) {
                    val astucesList = response.body()
                    Log.d("bien", "onResponse: Successful, Astuces count: ${astucesList?.size}")

                    astucesList?.forEach { astuce ->
                        Log.d("Image URL", "Image URL: ${astuce.imageItemA}")
                    }

                    astuces.value = astucesList!!
                } else {
                    Log.d("mechekel", "onResponse: Unsuccessful, Code: ${response.code()}")
                    errorMessage.value = "mechekel: ${response.code()}"
                }
            }


            override fun onFailure(call: Call<List<Astuce>>, t: Throwable) {
                Log.e("maredh", "onFailure: ${t.message}", t)
                errorMessage.value = "maredh: ${t.message}"
            }
        })
    }
}
