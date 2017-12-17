package com.codingdojoangola.ui.blog

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by pedromassango on 12/16/17.
 */
class PostAdapter(private val activity: Activity, private val iClickListener: IClickListener<Post>,private val iRequestComments: Callbacks.IRequestComments) : RecyclerView.Adapter<PostVH>() {

    var posts = arrayListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PostVH {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_post, parent, false)
        return PostVH(view)
    }

    override fun onBindViewHolder(holder: PostVH?, position: Int) {
       val post = posts[position]
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Fill the current item post
        holder!!.bindViews(post, iClickListener, iRequestComments)

        holder.view.findViewById<View>(R.id.root_view_blog)
                .setOnLongClickListener{

                    if(userId == post.authorId) {
                        AlertDialog.Builder(activity)
                                .setTitle("Actualizar/Eliminar")
                                .setMessage("Oque deseja fazer? Escolha uma opcao abaixo.")
                                .setNegativeButton("Actualizar", { dialog, witch -> updatePost(post)})
                                .setPositiveButton("Eliminar", { dialog, witch -> deletePost(post, position)})
                    }
                    return@setOnLongClickListener true
                }
    }

    private fun updatePost(post: Post) {

        val i = Intent(activity, NewPostActivity::class.java)
        i.putExtra(PostVH.POST_EXTRA_KEY, post)

        activity.startActivity( i)
    }

    private fun deletePost(post: Post, position: Int) {
        FirebaseDatabase.getInstance().reference
                .child("posts")
                .child(post.id)
                .setValue(null)
                .addOnFailureListener{
                    Toast.makeText(activity, "Falha ao eliminar artigo. tente novamente.", Toast.LENGTH_LONG).show() }
                .addOnSuccessListener { remove( position) }

    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun remove(position: Int){
        synchronized(PostAdapter::class.java){
            posts.removeAt( position)
            notifyItemRemoved(position)
        }
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