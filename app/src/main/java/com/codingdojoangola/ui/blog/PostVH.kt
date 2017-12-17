package com.codingdojoangola.ui.blog

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Comment
import com.codingdojoangola.models.split.blog.Post
import com.google.firebase.database.*
import java.util.*

/**
 * Created by pedromassango on 12/16/17.
 */
class PostVH(val view: View) : RecyclerView.ViewHolder(view), Callbacks.GetAll<Comment> {

    companion object {
        val POST_EXTRA_KEY = "POST_EXTRA_KEY"
    }

    private var commentsVisible: Boolean = false
    private var haveComments: Boolean = false

    val context = view.context

    val commentsAdapter: CommentAdapter
    private var tv_title: TextView
    private var tv_date: TextView
    private var tv_content: TextView
    private var tv_author: TextView
    private var tv_no_comments: TextView
    private var btn_comment: Button
    private var recyclerView: RecyclerView
    private var layout_comments: View

    private lateinit var post: Post
    /*
        Function to fill blog row.
     */
    fun bindViews(post: Post, iCommentButtonClickedListener: IClickListener<Post>, iRequestComments: Callbacks.IRequestComments) {
        this.post = post

        val postDate = TimeUtil().getTimeAgo( post.date)

        tv_title.text = post.title
        tv_content.text = post.content
        tv_author.text = post.author
        tv_date.text = postDate

        // Post action click
        view.findViewById<View>(R.id.root_view_blog).setOnClickListener {
            if (commentsVisible) {
                recyclerView.visibility = View.GONE
                tv_no_comments.visibility = View.GONE
                layout_comments.visibility = View.GONE
                commentsVisible = false

                addListener(post, false)
            } else {
                layout_comments.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
                commentsVisible = true

                addListener(post, true)
            }

            if (!haveComments) {
                tv_no_comments.visibility = View.VISIBLE
                tv_no_comments.text = "Carregando comentarios..."
                commentsVisible = true

                iRequestComments.getComments(post.id, this)
            }
        }

        // Comment button clicked
        btn_comment.setOnClickListener {
            // Notify the fragment to handle comment click
            iCommentButtonClickedListener.onClick(post)
        }

        layout_comments.setOnClickListener { layout_comments.visibility = View.GONE }
    }

    private fun addListener(post: Post, enable: Boolean) {

        commentsAdapter.clear()

        val eventListener: ChildEventListener
        val commentsRef = FirebaseDatabase.getInstance().reference
                .child("comments")
                .child(post.id)

        eventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.v("output", "comment: added ")

                val c = p0!!.getValue(Comment::class.java)

                commentsAdapter.add(c!!)

                Log.v("output", "comment: $c")
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                Log.v("output", "comment: removed")

                val c = p0!!.getValue(Comment::class.java)

                commentsAdapter.remove(c!!)
            }
        }

        if (enable)
            commentsRef.addChildEventListener(eventListener)
        else
            commentsRef.removeEventListener(eventListener)
    }

    override fun onSuccess(results: ArrayList<Comment>) {

        commentsVisible = true

        if (results.isEmpty()) {
            tv_no_comments.visibility = View.VISIBLE
            tv_no_comments.text = "Sem comentarios."
            return
        }

        haveComments = true

        layout_comments.visibility = View.VISIBLE
        tv_no_comments.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        commentsAdapter.add(results)

        addListener(post, true)
    }

    override fun onError() {

        tv_no_comments.visibility = View.VISIBLE
        tv_no_comments.text = "Falha ao obter comentarios."
        commentsVisible = true
    }

    init {
        btn_comment = view.findViewById(R.id.button_comment)

        tv_author = view.findViewById(R.id.textview_blog_autor)
        tv_content = view.findViewById(R.id.textview_blog_conteudo)
        tv_date = view.findViewById(R.id.textview_blog_date)
        tv_title = view.findViewById(R.id.textview_blog_title)

        layout_comments = view.findViewById<View>(R.id.layout_comments)
        tv_no_comments = view.findViewById(R.id.not_comments)
        recyclerView = view.findViewById(R.id.recyclerview_comments)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        commentsAdapter = CommentAdapter(context as Activity)

        recyclerView.adapter = commentsAdapter
        recyclerView.hasFixedSize()
    }
}