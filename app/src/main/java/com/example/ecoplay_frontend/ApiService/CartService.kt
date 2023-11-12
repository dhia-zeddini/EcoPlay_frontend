package com.example.ecoplay_frontend.ApiService

import com.example.ecoplay_frontend.model.ProductModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface CartService {

    @POST("carts/get")
    fun getProductsInCart(@Body cartIdRequest: CartIdRequest): Call<List<ProductModel>>

    @PUT("carts/adPtC")
    fun addToCart(@Body addToCartRequest: AddToCartRequest): Call<AddToCartResponse>

    @POST("carts/adPtC") // Assume this is your endpoint for adding a product
    fun addProductToCart(@Body addProductToCartRequest: AddProductToCartRequest): Call<ResponseMessage>
///////////////////

    @POST("total/calculateCartTotal")
    fun calculateCartTotal(@Body request: CalculateCartTotalRequest): Call<CalculateCartTotalResponse>

}
  /////////

data class CalculateCartTotalResponse(val message: String, val total: Double, val cart: Any) // Replace 'Any' with a proper cart model if needed.
data class CalculateCartTotalRequest(val cartId: String)

//////////////////////////
data class CartIdRequest(val cartId: String)
data class ResponseMessage(val message: String)

data class AddProductToCartRequest(
    val cartId: String,
    val productId: String
)

data class AddToCartRequest(
    val cartId: String,
    val productId: String
)

data class AddToCartResponse(
    val message: String
)


