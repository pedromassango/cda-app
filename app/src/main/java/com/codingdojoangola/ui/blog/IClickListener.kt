package com.codingdojoangola.ui.blog

/**
 * Created by pedromassango on 12/16/17.
 */
/*
    Class to listen click from adapter to Activity and fragments.
 */
interface IClickListener<T> {

    fun onClick(data :T)
}