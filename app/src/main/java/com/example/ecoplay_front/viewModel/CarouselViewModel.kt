package com.example.ecoplay_front.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoplay_front.apiService.RetrofitInstance
import com.example.ecoplay_front.model.Challenge
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarouselViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> = _products
    val prod = MutableLiveData<ProductModel>()

    fun fetchProductsFromBackend() {
        /*viewModelScope.launch {
            try {
                val productList = withContext(Dispatchers.IO) {
                    RetrofitInstance.carouselleService.getAllProducts()

                }
                Log.d("background fetch","mrigllll ${productList}")
                _products.value=productList
            } catch (e: Exception) {
                _products.postValue(emptyList())
                // Optionally log the exception or handle the error state
                Log.d("background fetch","moch mrigllll ${e.message}")
            }
        }*/
            RetrofitInstance.carouselleService.getAllProducts().enqueue(object : Callback<List<ProductModel>> {
                override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>>) {
                    if (response.isSuccessful) {
                        _products.value = response.body()
                        Log.d("RetrofitCall", "Produit Response successful: ${response.body()}")

                    }
                }
                    override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                        _products.value = emptyList()
                    }
                }
            )


    }

    fun fetchProductById(prodId:String) {
        viewModelScope.launch {
            try {
                val product = withContext(Dispatchers.IO) {
                    RetrofitInstance.carouselleService.getProductById(prodId)
                }
                prod.value=product
                Log.d("Retro","mrigllllll ${product}")

            } catch (e: Exception) {
                _products.postValue(emptyList())
                // Optionally log the exception or handle the error state
            }
        }
    }
}
