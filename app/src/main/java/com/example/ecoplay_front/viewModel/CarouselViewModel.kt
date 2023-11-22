package com.example.ecoplay_front.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecoplay_front.apiService.RetrofitInstance
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CarouselViewModel : ViewModel() {
    private val _products = MutableLiveData<List<ProductModel>>()
    val products: LiveData<List<ProductModel>> = _products
    val prod = MutableLiveData<ProductModel>()

    fun fetchProductsFromBackend() {
        viewModelScope.launch {
            try {
                val productList = withContext(Dispatchers.IO) {
                    RetrofitInstance.carouselleService.getAllProducts()

                }
                Log.d("background fetch","mrigl ${productList}")
                _products.postValue(productList)
            } catch (e: Exception) {
                _products.postValue(emptyList())
                // Optionally log the exception or handle the error state
            }
        }
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
