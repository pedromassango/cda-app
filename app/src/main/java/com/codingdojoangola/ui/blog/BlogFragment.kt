package com.codingdojoangola.ui.blog

import android.app.Activity
import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * Created by pedromassango on 12/16/17.
 */
/*
    This class
 */
class BlogFragment(val activity: Activity, val view: View) {

    // The Post Adapter to adapt posts
    val postsAdapter = PostAdapter( activity)

    // Temp list to store datas from server
    val posts = arrayListOf<Post>()

    init {

        // The recycler view to show Posts
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_view_blog)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = postsAdapter

        // FAB click to create a new post
        view.findViewById<FloatingActionButton>(R.id.fab_new_post)
                .setOnClickListener {
                    activity.startActivity(Intent(activity, NewPostActivity::class.java))
                }

        // Show the data
        setup()
    }

    fun setup() {

        // TODO: remove temp data

        // Temp data
        val post = Post()
        post.author = "Pedro Massango"
        post.content = "Simple title Post. Loren ipsun, Loren ipsun, Loren ipsun, Loren ipsun, Loren ipsun, Loren ipsun\n" +
                "Loren ipsun."
        post.date = System.currentTimeMillis()
        post.title = "Um Titulo de exemplo"

        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)
        postsAdapter.add(post)

        // Fill the posts lists and recyclerview
        getPostsFromServer()
    }

    /*
        Method to get the data from server and show in recyclerview
     */
    private fun getPostsFromServer() {
        val postsRef = FirebaseDatabase.getInstance().getReference().child("posts")
        postsRef.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot?) {
                if(!snapshot!!.exists()){
                    return
                }

                for(shot in snapshot.children){
                    //Convert received post to data class
                    val post = shot.getValue(Post::class.java)

                    // Add received data to list
                    posts.add( post!!)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
                Toast
                        .makeText(activity, "Algo de errado aconteceu tente novamente!", Toast.LENGTH_LONG)
                        .show()
            }

        })
    }
}