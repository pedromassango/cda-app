package com.codingdojoangola.ui.blog

import com.codingdojoangola.models.split.blog.Comment
import com.codingdojoangola.models.split.blog.Post

/**
 * Created by pedromassango on 12/17/17.
 */
class Callbacks {

    interface IRequestComments {
        fun getComments(postId: String, getAll: GetAll<Comment>)
    }

    interface GetAll<T>{
        fun onSuccess(results: ArrayList<T>)
        fun onError()
    }
}