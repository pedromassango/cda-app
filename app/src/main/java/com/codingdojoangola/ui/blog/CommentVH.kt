package com.codingdojoangola.ui.blog

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import com.codingdojoangola.R
import com.codingdojoangola.models.split.blog.Comment
import com.squareup.picasso.Picasso
import java.util.*

/**
 * Created by pedromassango on 12/17/17.
 */
class CommentVH(val view: View) : RecyclerView.ViewHolder(view) {

    private var tv_date: TextView
    private var tv_content: TextView
    private var tv_author: TextView
    private val imgAuthor: ImageView

    /*
        Function to fill comment row.
     */
    fun bindViews(comment: Comment) {
        val commentDate = TimeUtil().getTimeAgo( comment.date)

        tv_content.text = comment.content
        tv_author.text = comment.author
        tv_date.text = commentDate

        if (URLUtil.isValidUrl(comment.authorPhotoUrl)) {
            Picasso.with(view.context)
                    .load(comment.authorPhotoUrl)
                    .into(imgAuthor)
        }
    }

    init {

        imgAuthor = view.findViewById(R.id.img_comment_author)
        tv_author = view.findViewById(R.id.textview_author_comment)
        tv_content = view.findViewById(R.id.textView_comment_content)
        tv_date = view.findViewById(R.id.textView_comment_date)
    }
}