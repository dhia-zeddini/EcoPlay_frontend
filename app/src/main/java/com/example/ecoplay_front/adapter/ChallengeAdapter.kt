package com.example.ecoplay_front.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.Challenge

class ChallengeAdapter(
    private var challenges: List<Challenge>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    fun setChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_challenge_adapter, parent, false)
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


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(challenges[position])
                }
            }
        }

        fun bind(challenge: Challenge) {
            titleTextView.text = challenge.title
            descriptionTextView.text = challenge.description

            Glide.with(itemView.context).load(challenge.media).into(imageView)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(challenge: Challenge)
    }
}
