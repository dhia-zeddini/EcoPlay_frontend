// Define the package
package com.example.ecoplay_front.viewModel

// Import necessary libraries and classes
import android.util.Log
import com.example.ecoplay_front.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.model.Challenge

// Extend RecyclerView.Adapter for functionality
class ChallengeAdapter : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    // List of challenges to be displayed
    private var challenges: List<Challenge> = emptyList()

    // Method to update the list of challenges
    fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged() // Notify the adapter to reflect the changes in the dataset
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.challengeshome, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challenges[position]
        holder.bind(challenge)
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    inner class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(challenge: Challenge) {
            Log.d("ChallengeAdapter", "Media URL: ${challenge.media}")

            titleTextView.text = challenge.title
            descriptionTextView.text = challenge.description
            Glide.with(itemView.context)
                .load(challenge.media)
                .into(imageView)
        }
    }
}
