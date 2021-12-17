package com.example.navigationdrawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private val exampleContacts: List<ExamplePost>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eLogin: TextView
        val eDate: TextView
        val eContent: TextView

        init {
            eLogin = itemView.findViewById(R.id.loginView)
            eDate = itemView.findViewById(R.id.dateView)
            eContent = itemView.findViewById(R.id.contentView)
        }
    }

    var mExamplePost: List<ExamplePost>

    init{
        mExamplePost = exampleContacts
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.post_item, parent, false)
        val cvh = PostViewHolder(view)

        return cvh
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = mExamplePost.get(position)

        holder.eLogin.text = currentItem.login
        holder.eDate.text = currentItem.date
        holder.eContent.text = currentItem.content
    }

    override fun getItemCount(): Int {
        return mExamplePost.size
    }
}