package com.codingdojoangola.ui.blog

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import com.codingdojoangola.R
import com.codingdojoangola.app.CDA
import com.codingdojoangola.models.split.blog.Comment
import com.codingdojoangola.models.split.blog.Post
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

/**
 * Created by pedromassango on 12/17/17.
 */
class BlogPostDetailActivity : AppCompatActivity(), OnFailureListener, OnSuccessListener<Void> {

    lateinit var post: Post
    lateinit var app: CDA

    lateinit var commentsAdapter: CommentAdapter

    private lateinit var tv_title: TextView
    private lateinit var tv_date: TextView
    private lateinit var tv_content: TextView
    private lateinit var tv_author: TextView
    private lateinit var tv_no_comments: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var layout_comments: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_description)

        // Getiing views
        tv_author = findViewById(R.id.textview_blog_autor)
        tv_content = findViewById(R.id.textview_blog_conteudo)
        tv_date = findViewById(R.id.textview_blog_date)
        tv_title = findViewById(R.id.textview_blog_title)
        layout_comments = findViewById<View>(R.id.layout_comments)
        tv_no_comments = findViewById(R.id.not_comments)
        recyclerView = findViewById(R.id.recyclerview_comments)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        commentsAdapter = CommentAdapter(this)
        recyclerView.adapter = commentsAdapter
        recyclerView.hasFixedSize()

        findViewById<View>(R.id.fab_new_post)
                .setOnClickListener { commentPost() }

        // Binding data

        post = intent.getParcelableExtra<Post>(PostVH.POST_EXTRA_KEY)

        tv_title.text = post.title
        tv_date.text = TimeUtil().getTimeAgo(post.date)
        tv_content.text = post.content

        if (!CDA.getData().isEmpty()) {
            Log.v("output", " Comments is empty ")

            tv_no_comments.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            commentsAdapter.add(CDA.getData())
        }

        // Listem for comment from the others users
        addListener(post, true)
    }

    private fun commentPost() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.write_a_comment))

        // Set up the input
        val edComment = EditText(this)
        val view = LinearLayout(this)
        view.setPadding(18,32,8,32)
        view.addView( edComment)

        edComment.hint = String.format("%s: %s", getString(R.string.commenting_in),  post.title)

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        edComment.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        edComment.minLines = 2

        builder.setView(view)

        // Set up the buttons
        builder.setPositiveButton(getString(R.string.str_comment)) { dialog, which ->
            if(edComment.text.toString().isEmpty()){
                Toast.makeText(this, getString(R.string.write_something), Toast.LENGTH_LONG).show()
                return@setPositiveButton
            }

            commentOnPost(edComment.text.toString())
        }

        builder.show()
    }

    private fun commentOnPost(commentText: String) {

        var username = FirebaseAuth.getInstance().currentUser!!.displayName ?: "Anonimo"
        val userPhoto = FirebaseAuth.getInstance().currentUser!!.photoUrl.toString()

        if(username.isEmpty()){
            username = String.format("Anonimo %s", System.currentTimeMillis().toString().substring(0, 3))
        }

        val comment = Comment(
                id = FirebaseDatabase.getInstance().reference.push().key,
                author = username,
                authorPhotoUrl = userPhoto,
                content = commentText)

        FirebaseDatabase.getInstance().reference
                .child("comments")
                .child(post.id)
                .child(comment.id)
                .setValue(comment)
                .addOnFailureListener(this)
                .addOnSuccessListener(this)
    }

    private fun addListener(post: Post, enable: Boolean) {
        Log.v("output", " addListener ")
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

            var added = false
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                Log.v("output", "comment: added ")

                val c = p0!!.getValue(Comment::class.java)
                commentsAdapter.add(c!!)

                if (added) {
                    tv_no_comments.visibility = View.GONE
                }

                added = true

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

    /*
        Method to notify when a comment action was successful.
     */
    override fun onSuccess(p0: Void?) {
        Toast.makeText(this, getString(R.string.comment_send_success), Toast.LENGTH_LONG)
                .show()
    }


    /*
        Save Comment action error.
     */
    override fun onFailure(p0: Exception) {
        AlertDialog.Builder(this)
                .setTitle(getString(R.string.str_fail))
                .setMessage(getString(R.string.str_check_internet))
                .setPositiveButton(getString(R.string.close), null)
    }

}