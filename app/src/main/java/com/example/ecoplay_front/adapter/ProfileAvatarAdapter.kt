package com.example.ecoplay_front.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.ProductModel
import com.example.ecoplay_front.uttil.Constants
import com.example.ecoplay_front.view.ProductDetails

class ProfileAvatarAdapter (private var products: MutableList<ProductModel>) :
    RecyclerView.Adapter<ProfileAvatarAdapter.ProfileAvatarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAvatarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_avatars_single_item, parent, false)
        return ProfileAvatarViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileAvatarViewHolder, position: Int) {
        val item = products[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = products.size

    class ProfileAvatarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.avatarImage)
        private val titleView: TextView = itemView.findViewById(R.id.avatarName)

        private val context = itemView.context // Context for Glide

        fun bind(product: ProductModel) {
            titleView.text = product.nameP
            Glide.with(context)
                .load("${Constants.BASE_URL}images/product/" + product.image)
                .into(imageView)


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
