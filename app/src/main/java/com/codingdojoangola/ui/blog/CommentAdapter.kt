package com.codingdojoangola.ui.blog

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Comment

/**
 * Created by pedromassango on 12/16/17.
 */
class CommentAdapter(private val activity: Activity) : RecyclerView.Adapter<CommentVH>() {

    var comments = arrayListOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CommentVH {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_blog_comment, parent, false)
        return CommentVH(view)
    }

    override fun onBindViewHolder(holder: CommentVH?, position: Int) {
       val comment = comments[position]

        // Fill the current item comment
        holder!!.bindViews(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    /*
        Method to insert a single post in list.
     */
    fun add(comment: Comment){
        synchronized(CommentAdapter::class.java){
            comments.add(comment)
            notifyDataSetChanged()
        }
    }

    /*
       Method to insert a post list in recyclerView.
    */
    fun add(mComments: List<Comment>){
        synchronized(CommentAdapter::class.java){
            comments.clear()
            comments.addAll(mComments)
            notifyDataSetChanged()
        }
    }

    fun remove(c: Comment) {
        synchronized(CommentAdapter::class.java){
            comments.remove(c)
            notifyDataSetChanged()
        }
    }

    fun clear() {
        synchronized(CommentAdapter::class.java){
            comments.clear()
            notifyDataSetChanged()
        }
    }

}