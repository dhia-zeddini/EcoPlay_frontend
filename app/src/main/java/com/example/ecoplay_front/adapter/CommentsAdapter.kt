package com.example.ecoplay_front.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecoplay_front.R
import com.example.ecoplay_front.model.Comment

class CommentsAdapter(
    private var comments: MutableList<Comment>,
    private val listener: OnItemClickListener? = null
) : RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(comment: Comment)
    }

    fun setComments(newComments: List<Comment>) {
        this.comments = newComments.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_comment, parent, false)
        return CommentViewHolder(view)
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClick(comments[position])
        }
    }

    override fun getItemCount(): Int = comments.size

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_challenge_title)
        private val subtitleTextView: TextView = itemView.findViewById(R.id.text_challenge_subtitle)
        private val imageView: ImageView = itemView.findViewById(R.id.image_challenge)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition // Or use layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(comments[position])
                }
            }
        }


        override fun onClick(v: View?) {
            listener?.onItemClick(comments[adapterPosition])
        }

        fun bind(comment: Comment) {
            titleTextView.text = comment.title
            subtitleTextView.text = comment.description
            val imageUrl = if (!comment.image.isNullOrBlank()) {
                "http://192.168.1.115:9001/images/challenges/${comment.image}"
            } else {
                null
            }

            // Use Glide to load the image
            if (imageUrl != null) {
                Glide.with(itemView.context)
                    .load(imageUrl)
                    .into(imageView)
            } else {
                imageView.visibility = View.GONE // Or set a default image
            }
        }
    }
}
