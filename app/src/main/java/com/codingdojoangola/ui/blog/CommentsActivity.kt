package com.codingdojoangola.ui.blog

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Post
import java.util.*

/**
 * Created by pedromassango on 12/16/17.
 */
class CommentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        // Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Getting data from giving Intent
        val post = intent.getParcelableExtra<Post>(PostVH.POST_EXTRA_KEY)
        val date = Date(post.date)
        val postDate = String.format("%s/%s/%s", date.day, date.month, date.year)

        title = post.author
        supportActionBar!!.subtitle = postDate
    }
}