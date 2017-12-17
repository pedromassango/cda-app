package com.codingdojoangola.ui.blog

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

/**
 * Created by pedromassango on 12/16/17.
 */
class NewPostActivity : AppCompatActivity(), OnFailureListener, OnSuccessListener< Void> {

    lateinit var edTitle: EditText
    lateinit var edContent: EditText
    lateinit var publishDialog: ProgressDialog
    lateinit var post: Post
    var update: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_blog_post)

        // Getting toolbar from XML
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Changing Activity toolbar title color
        toolbar.setTitleTextColor(resources.getColor(R.color.white))

        // Finding views by Id
        edTitle = findViewById(R.id.edt_new_post_title)
        edContent = findViewById(R.id.edt_new_post_content)

        // ProgressDialog initialization
        publishDialog = ProgressDialog(this)
        publishDialog.isIndeterminate = true
        publishDialog.setTitle("Publicando...")
        publishDialog.setCancelable(false)

        if(intent.hasExtra(PostVH.POST_EXTRA_KEY)){
            title = "Actualizar"
            publishDialog.setMessage("Actualizando...")

            post = intent.getParcelableExtra(PostVH.POST_EXTRA_KEY)
            update = true

            edTitle.setText(post.title)
            edContent.setText( post.content)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.activity_new_blog_post_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item!!.itemId == R.id.action_publish_post) {
            // Will save the post in server
            publishPost()
            return false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun publishPost() {
        val title = edTitle.text.toString()
        val content = edContent.text.toString()

        // check if Title is empty
        if (title.trim().isBlank()) {
            showToast("Adicione algum titulo!")
            return
        }

        // check if Content is empty
        if (content.trim().isBlank()) {
            showToast("Adicione alguma descricao!")
            return
        }

        // Now we can show the publish progress
        publishDialog.show()

        // If there is no user authenticated, then username will be Anonymous
        val author = FirebaseAuth.getInstance().currentUser?.displayName ?: "Anonimo"
        val authorId = FirebaseAuth.getInstance().currentUser?.uid

        // Generating post model to save
        val post = Post(title = title, content = content, author = author, authorId = authorId!!)

        if(update){
            post.id = this.post.id
        }

        // Store the post in database
        val postsRef = FirebaseDatabase.getInstance().reference.child("posts")
        postsRef.child(post.id).setValue(post)
                .addOnFailureListener(this)
                .addOnSuccessListener(this)
    }

    private fun showToast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()

    override fun onSuccess(p0: Void?) {
        publishDialog.dismiss()

        if (update){
            showToast("Actualizado.")
        }else {
            showToast("Publicado!")
        }

        // Close this activity
        this.finish()
    }

    /*
        Method called when publish errors occours.
     */
    override fun onFailure(p0: Exception) {
        publishDialog.dismiss()

        showPublishError(p0.message)
    }

    /*
        Method to show the publish error.
     */
    private fun showPublishError(message: String?) {
        AlertDialog.Builder(this)
                .setTitle("Falha!")
                .setMessage(message ?: "Algo de errado aconteceu, tente novamente!")
                .setPositiveButton("OK", null)
    }
}