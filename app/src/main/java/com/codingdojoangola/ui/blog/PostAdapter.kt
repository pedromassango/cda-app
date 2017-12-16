package com.codingdojoangola.ui.blog

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post

/**
 * Created by pedromassango on 12/16/17.
 */
class PostAdapter(private val activity: Activity) : RecyclerView.Adapter<PostVH>() {

    var posts = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostVH {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_post, parent, false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH?, position: Int) {
       val post = posts[position]

        // Fill the current item post
        holder!!.bindViews(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    /*
        Method to insert a single post in list.
     */
    fun add(post: Post){
        synchronized(PostAdapter::class.java){
            posts.add(post)
            notifyDataSetChanged()
        }
    }

    /*
       Method to insert a post list in recyclerView.
    */
    fun add(mPosts: List<Post>){
        synchronized(PostAdapter::class.java){
            posts.clear()
            posts.addAll(mPosts)
            notifyDataSetChanged()
        }
    }

}