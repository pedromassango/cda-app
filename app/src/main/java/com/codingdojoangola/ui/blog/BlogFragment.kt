package com.codingdojoangola.ui.blog

import android.app.Activity
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Comment
import com.codingdojoangola.models.split.blog.Post
import com.codingdojoangola.server.network.NetworkServer
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


/**
 * Created by pedromassango on 12/16/17.
 */
/*
    This class
 */
class BlogFragment(val activity: Activity, val view: View) : IClickListener<Post>, OnFailureListener, OnSuccessListener<Void>, Callbacks.IRequestComments {

    // Root reference to save data
    private val rootRef = FirebaseDatabase.getInstance().getReference()

    //  VIEWS
    private var tvInfo: TextView = view.findViewById(R.id.textView_no_data_blog)
    private var progressBar: ProgressBar = view.findViewById(R.id.progress_bar_blog)

    // The Post Adapter to adapt posts
    private val postsAdapter = PostAdapter(activity, this, this)

    /*
        First method that will be called.
     */
    init {

        // The recycler view to show Posts
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_blog)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = postsAdapter

        // Textview info clicked to retry get post again
        tvInfo.setOnClickListener { getPostsFromServer() }

        // FAB click to create a new post
        view.findViewById<FloatingActionButton>(R.id.fab_new_post)
                .setOnClickListener {
                    activity.startActivity(Intent(activity, NewPostActivity::class.java))
                }

        // Get and show the data
        setupData()
    }

    /*
        Method called when a Post comment button was clicked.
     */
    override fun onClick(data: Post) {

        val builder = AlertDialog.Builder(activity)
        builder.setTitle(activity.getString(R.string.write_a_comment))

        // Set up the input
        val edComment = EditText(activity)
        val view = LinearLayout(activity)
        view.setPadding(18,32,8,32)
        view.addView( edComment)

        edComment.hint = String.format("%s: %s", activity.getString(R.string.commenting_in),  data.title)

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        edComment.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
        edComment.minLines = 2

        builder.setView(view)

        // Set up the buttons
        builder.setPositiveButton(activity.getString(R.string.str_comment)) { dialog, which ->
            if(edComment.text.toString().isEmpty()){
                Toast.makeText(activity, activity.getString(R.string.write_something), Toast.LENGTH_LONG).show()
                return@setPositiveButton
            }

            commentOnPost(data, edComment.text.toString())
        }

        builder.show()
    }

    /*
        Method to comment in a selected post.
     */
    private fun commentOnPost(mPost: Post, commentText: String) {

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

        rootRef.child("comments")
                .child(mPost.id)
                .child(comment.id)
                .setValue(comment)
                .addOnFailureListener(this)
                .addOnSuccessListener(this)
    }

    /*
        Method to notify when a comment action was successful.
     */
    override fun onSuccess(p0: Void?) {
    Toast.makeText(activity, activity.getString(R.string.comment_send_success), Toast.LENGTH_LONG)
                .show()
    }

    /*
        Retrieve comments from a clicked post
     */
    override fun getComments(postId: String, getAll: Callbacks.GetAll<Comment>) {

        val tmp = arrayListOf<Comment>()

        rootRef.child("comments")
                .child( postId)
                .limitToFirst(20)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                         getAll.onError()
                    }

                    override fun onDataChange(p0: DataSnapshot?) {

                        p0!!.children
                                .map { it.getValue(Comment::class.java) }
                                .mapTo(tmp) { it!! }

                        getAll.onSuccess(tmp)
                    }
                })
    }


    /*
        Comment action error.
     */
    override fun onFailure(p0: Exception) {
        AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.str_fails))
                .setMessage(activity.getString(R.string.str_comment_title_erro))
                .setPositiveButton(activity.getString(R.string.str_close), null)
    }


    private fun setupData() {

        if (NetworkServer.getInstance().isNetworkAvailable) {
            Log.v("output", "setupData, get from server")
            // Fill the posts lists and recyclerview
            getPostsFromServer()
            return
        }

        progressBar.visibility = View.GONE
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = activity.getString(R.string.nothing_to_show)
    }

    /*
        Method to get the data from server and show in recyclerview
     */
    private fun getPostsFromServer() {
        // show progress bar
        showLoadProgress(true)
        tvInfo.visibility = View.GONE

        val postsRef = rootRef.child("posts")
        postsRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot?) {

                // Hide progressBar
                showLoadProgress(false)

                if (!snapshot!!.exists()) {

                    tvInfo.visibility = View.VISIBLE
                    tvInfo.text = activity.getString(R.string.nothing_to_show)
                    return
                }

                Log.v("output", "SNAPSHOST: $snapshot")

                for (shot in snapshot.children) {
                    //Convert received post to data class
                    val post = shot.getValue(Post::class.java)

                    Log.v("output", "SHOST: $shot")

                    Log.v("output", "POST: $post")
                    // Add received data to list
                    postsAdapter.add(post!!)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                // Hide progressBar
                showLoadProgress(false)

                tvInfo.visibility = View.VISIBLE
                tvInfo.text = activity.getString(R.string.something_goe_wrong)
            }

        })
    }

    /*
        Show/Hide the load data component.
     */
    private fun showLoadProgress(b: Boolean) {
        progressBar.visibility =
                if (b) View.VISIBLE
                else View.GONE
    }
}