package com.example.ecoplay_frontend.viewModel

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import java.io.Serializable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecoplay_frontend.R
import com.bumptech.glide.Glide
import com.example.ecoplay_frontend.model.ProductModel
import com.example.ecoplay_frontend.view.ProductDetails

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

            // Use Glide to load the image from the URL
            // Make sure to call 'Glide.with' using a valid context, 'itemView' is used here for simplicity
            Glide.with(itemView.context)
                .load(product.image)
                .into(imageView)
            Glide.with(context).load("http://192.168.99.207:8088/images/product/"+product.image).into(imageView)




            addToCartButton.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetails::class.java)
                intent.putExtra("product", product)
                context.startActivity(intent)
                if (context is Activity) {
                    context.finish()
                }
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
