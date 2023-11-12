package com.example.ecoplay_frontend.viewModel

// Import necessary libraries and classes
import android.util.Log
import com.example.ecoplay_frontend.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_frontend.model.ProductModel

// Extend RecyclerView.Adapter for functionality
class CartAdapter : RecyclerView.Adapter<CartAdapter.ChallengeViewHolder>() {

    private var cartList: List<ProductModel> = listOf()

    fun setCartList(cartList: List<ProductModel>) {
        this.cartList = cartList
        notifyDataSetChanged()
        Log.d("RETROFIT","setList :$cartList")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.bind(cartList[position])
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView) // Replace with your actual TextView ID
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val context = itemView.context // Context for Glide
// Replace with your actual TextView ID

        fun bind(cartItem: ProductModel) {
            // Assuming you want to display the first product's name if available
            if (cartItem._id.isNotEmpty()) {

                titleTextView.text = cartItem.nameP
                descriptionTextView.text = cartItem.priceP.toString()
                Glide.with(context).load("http://192.168.99.207:8088/images/product/"+cartItem.image).into(imageView)

            } else {
                titleTextView.text = "No product"
                //descriptionTextView.text = cartItem.totalC.toString()
            }
        }

    }
}
