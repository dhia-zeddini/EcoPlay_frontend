package com.example.ecoplay_front.apiService

import com.example.ecoplay_front.model.AddProductToCartRequest
import com.example.ecoplay_front.model.AddToCartRequest
import com.example.ecoplay_front.model.AddToCartResponse
import com.example.ecoplay_front.model.CalculateCartTotalRequest
import com.example.ecoplay_front.model.CalculateCartTotalResponse
import com.example.ecoplay_front.model.CartIdRequest
import com.example.ecoplay_front.model.CreatePaymentIntentRequest
import com.example.ecoplay_front.model.PaymentIntentResponse
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.model.RemoveProductRequest
import com.example.ecoplay_front.model.ResponseMessage
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService {
    @POST("carts/get")
    fun getProductsInCart(@Body cartIdRequest: CartIdRequest): Call<List<ProductModel>>
    @PUT("carts/adPtC")
    fun addToCart(@Body addToCartRequest: AddToCartRequest): Call<AddToCartResponse>
    @POST("carts/adPtC") // Assume this is your endpoint for adding a product
    fun addProductToCart(@Body addProductToCartRequest: AddProductToCartRequest): Call<ResponseMessage>
    @POST("carts/total")
    fun calculateCartTotal(@Body request: CalculateCartTotalRequest): Call<CalculateCartTotalResponse>
    @POST("carts/pay")
    fun createPaymentIntent(
        @Body createPaymentIntentRequest: CreatePaymentIntentRequest
    ): Call<PaymentIntentResponse>
    @HTTP(method = "DELETE", path = "carts/rmPtc", hasBody = true)
    fun deleteProductFromCart(@Body body: RemoveProductRequest): Call<ResponseMessage>
}



