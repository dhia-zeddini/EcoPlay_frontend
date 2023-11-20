package com.exemple.ecoplay_front.viewModel

// Define the package

// Import necessary libraries and classes
import android.content.Intent
import android.util.Log
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



// Extend RecyclerView.Adapter for functionality
class AstuceAdapter : RecyclerView.Adapter<AstuceAdapter.AstuceViewHolder>() {
    //var onItemClick: ((Astuce) -> Unit)? = null

    // List of astuces to be displayeda
    private var astuce: List<Astuce> = emptyList()

    // Method to update the list of astuces
    fun setAstuce(astuces: List<Astuce>) {
        this.astuce = astuces
        notifyDataSetChanged() // Notify the adapter to reflect the changes in the dataset
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AstuceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_astuce, parent, false)
        return AstuceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AstuceViewHolder, position: Int) {
        val astuce = astuce[position]
        holder.bind(astuce)
    }

    override fun getItemCount(): Int {
        return astuce.size
    }

    inner class AstuceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleATextView: TextView = itemView.findViewById(R.id.titleA)
        private val sujetATextView: TextView = itemView.findViewById(R.id.sujetA)
        private val imageAView: ImageView = itemView.findViewById(R.id.imageItemA)
        private val astuceSingleLayout:LinearLayout=itemView.findViewById(R.id.acsuceSingleLayout)
        private val context = itemView.context

        fun bind(astuce: Astuce) {
            Log.d("esem image", "Media URL: ${astuce.imageItemA}")

            titleATextView.text = astuce.titleA
            sujetATextView.text = astuce.sujetA

            val imageUrl = astuce.imageItemA

            Log.d("esem image load", "Media URL: ${imageUrl}")


            Glide.with(itemView.context)
                .load(imageUrl)
                .into(imageAView)
            astuceSingleLayout.setOnClickListener{
                val intent = Intent(itemView.context, AstuceDetailsActivity::class.java)
                intent.putExtra("ASTUCE", astuce) // Pass any necessary data to AstuceDetailsActivity
                itemView.context.startActivity(intent)

        }

        }
    }
}
