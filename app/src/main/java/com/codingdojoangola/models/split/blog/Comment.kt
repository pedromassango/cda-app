package com.codingdojoangola.models.split.blog

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by pedromassango on 12/16/17.
 */
@IgnoreExtraProperties
data class Comment(var id: String = FirebaseDatabase.getInstance().reference.push().key,
                   var author: String,
                   var authorPhotoUrl: String,
                   var content: String,
                   var date: Long = System.currentTimeMillis()) {

    constructor(): this(author = "",authorPhotoUrl = "", content = "")
}