import androidx.lifecycle.ViewModel
import com.example.ecoplay_front.apiService.CartService
import com.example.ecoplay_front.apiService.RetrofitInstance
import com.example.ecoplay_front.model.AddToCartRequest
import com.example.ecoplay_front.model.AddToCartResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailsViewModel : ViewModel() {
    // Create a function to add a product to the cart
    fun addToCart(token:String,productId: String, cartId: String, callback: (Boolean) -> Unit) {
        val service = RetrofitInstance.retrofit.create(CartService::class.java)

        // Create the request body
        val request = AddToCartRequest(cartId, productId)

        // Enqueue the call to asynchronously execute the PUT request
        service.addToCart("Bearer $token",request).enqueue(object : Callback<AddToCartResponse> {
            override fun onResponse(
                call: Call<AddToCartResponse>,
                response: Response<AddToCartResponse>
            ) {
                if (response.isSuccessful) {
                    // Notify the callback that the product was added successfully
                    callback(true)
                } else {
                    // Notify the callback that an error occurred
                    callback(false)
                }
            }

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                // Notify the callback that an error occurred
                callback(false)
            }
        })
    }
}
