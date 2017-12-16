package com.codingdojoangola.ui.blog

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post

/**
 * Created by pedromassango on 12/16/17.
 */
class PostVH(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val POST_EXTRA_KEY = "POST_EXTRA_KEY"
    }

    val context = view.context
    var tv_title: TextView
    var tv_date: TextView
    var tv_content: TextView
    var tv_author: TextView
    var btn_comment: Button

    /*
        Function to fill blog row.
     */
    fun bindViews(post: Post){

        tv_title.text = post.title
        tv_content.text = post.content
        tv_author.text = post.author
        //tv_date.text  //TODO: need to show the date

        // Comment button click
        btn_comment.setOnClickListener {

            val commentsIntent = Intent(context, CommentsActivity::class.java)
            commentsIntent.putExtra(POST_EXTRA_KEY, post)

            context.startActivity(commentsIntent)
        }
    }

    init {
        btn_comment = view.findViewById(R.id.button_comment)

        tv_author = view.findViewById(R.id.textview_blog_autor)
        tv_content = view.findViewById(R.id.textview_blog_conteudo)
        tv_date = view.findViewById(R.id.textview_blog_date)
        tv_title = view.findViewById(R.id.textview_blog_title)
        Log.i("output", "PostVH setup...")
    }
}