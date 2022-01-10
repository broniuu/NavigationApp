package com.example.navigationdrawer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array.get

class PostAdapter(private val examplePosts: ArrayList<ExamplePost>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(){
    lateinit var mListener: OnItemClickListener
    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class PostViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val eLogin: TextView
        val eDate: TextView
        val eContent: TextView

        init {
            eLogin = itemView.findViewById(R.id.loginView)
            eDate = itemView.findViewById(R.id.dateView)
            eContent = itemView.findViewById(R.id.contentView)
            itemView.setOnClickListener(object : View.OnClickListener{
                override fun onClick(v: View?) {
                    if (listener != null){
                        var position = adapterPosition
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position)
                        }
                    }

                }
            }
            )
        }
    }

    private var mExamplePost: ArrayList<ExamplePost> = examplePosts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).
                inflate(R.layout.post_item, parent, false)
        val cvh = PostViewHolder(view, mListener)

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

    fun removeAt(position: Int){
        mExamplePost.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getPost(position: Int) : ExamplePost {
        return examplePosts[position]
    }
}