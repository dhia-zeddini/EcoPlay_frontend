package com.exemple.ecoplay_front.viewModel

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.model.Astuce
import com.exemple.ecoplay_front.R
import com.exemple.ecoplay_front.view.AstuceDetailsActivity

class AstuceAdapter : RecyclerView.Adapter<AstuceAdapter.AstuceViewHolder>() {
    private var astuces: List<Astuce> = emptyList()

    fun setAstuces(astuces: List<Astuce>) {
        this.astuces = astuces
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstuceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_astuce, parent, false)
        return AstuceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AstuceViewHolder, position: Int) {
        val astuce = astuces[position]
        holder.bind(astuce)
    }

    override fun getItemCount(): Int {
        return astuces.size
    }

    inner class AstuceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleA)
        private val sujetTextView: TextView = itemView.findViewById(R.id.sujetA)
        private val imageView: ImageView = itemView.findViewById(R.id.imageItemA)
        private val astuceLayout: LinearLayout = itemView.findViewById(R.id.acsuceSingleLayout)

        fun bind(astuce: Astuce) {
            titleTextView.text = astuce.titleA
            sujetTextView.text = astuce.sujetA

            val imagePrefix = "https://ecoplay-api.onrender.com/images/astuce/"
            val imageUrl = imagePrefix + astuce.imageItemA
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(imageView)

            astuceLayout.setOnClickListener {
                val intent = Intent(itemView.context, AstuceDetailsActivity::class.java)
                intent.putExtra("ASTUCE", astuce)
                itemView.context.startActivity(intent)
            }
        }
    }
}
