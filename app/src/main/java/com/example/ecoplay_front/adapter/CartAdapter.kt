package com.example.ecoplay_front.adapter

import android.util.Log
import com.example.ecoplay_front.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.uttil.Constants

class CartAdapter : RecyclerView.Adapter<CartAdapter.ProductViewHolder>() {

    private var cartList: List<ProductModel> = listOf()

    fun setCartList(cartList: List<ProductModel>) {
        this.cartList = cartList
        notifyDataSetChanged()
        Log.d("RETROFIT","setList :$cartList")
    }
    fun getProductIdAtPosition(position: Int): String {
        return cartList[position]._id
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(cartList[position])
    }
    override fun getItemCount(): Int {
        return cartList.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val context = itemView.context

        fun bind(cartItem: ProductModel) {
            if (cartItem._id.isNotEmpty()) {
                titleTextView.text = cartItem.nameP
                descriptionTextView.text = cartItem.priceP.toString()
                Glide.with(context).load("${Constants.BASE_URL}images/product/"+cartItem.image).into(imageView)

            } else {
                titleTextView.text = "No product"
            }
        }

    }
}