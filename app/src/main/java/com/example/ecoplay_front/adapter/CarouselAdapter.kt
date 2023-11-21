package com.example.ecoplay_front.adapter

import com.example.ecoplay_front.view.ProductDetails
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_front.R
import com.bumptech.glide.Glide
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.uttil.Constants.BASE_URL

class CarouselAdapter (private var products: MutableList<ProductModel>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_carousel_item, parent, false)
        return CarouselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = products[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = products.size

    class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewCarouselItem)
        private val titleView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val addToCartButton: Button = itemView.findViewById(R.id.buttonAddToCart)
        private val context = itemView.context // Context for Glide

        fun bind(product: ProductModel) {
            titleView.text = product.nameP
            descriptionView.text = product.descriptionP
            Glide.with(context)
                .load("${BASE_URL}images/product/" + product.image)
                .into(imageView)

            addToCartButton.setOnClickListener {
                val intent = Intent(context, ProductDetails::class.java)
                intent.putExtra("product", product)
                context.startActivity(intent)
            }
        }
    }

    fun updateData(newData: List<ProductModel>) {
        // Clear the current product list
        products.clear()
        // Add all the new data to the list
        products.addAll(newData)
        // Notify the adapter that the data set has changed to refresh the RecyclerView
        notifyDataSetChanged()
    }
}
