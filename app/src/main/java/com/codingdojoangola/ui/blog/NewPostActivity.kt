package com.codingdojoangola.ui.blog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.codingdojoangola.R

/**
 * Created by pedromassango on 12/16/17.
 */
class NewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_blog_post)

        // Getting toolbar from XML
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.activity_new_blog_post_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if(item!!.itemId == R.id.action_publish_post){
            Toast.makeText(this, "Publicando...", Toast.LENGTH_LONG).show()
            return false
        }
        return super.onOptionsItemSelected(item)
    }
}