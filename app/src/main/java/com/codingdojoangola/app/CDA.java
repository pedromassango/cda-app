package com.codingdojoangola.app;

//:::::::::::::::: Android imports
import android.app.Application;
import android.util.Log;

//:::::::::::::::: Import from third parties (com, junit, net, org)


//:::::::::::::::: Java and javax


//:::::::::::::::: Same project import
import com.codingdojoangola.models.split.blog.Comment;
import com.codingdojoangola.server.network.NetworkServer;
import com.codingdojoangola.ui.split.MainDrawer;

import java.util.ArrayList;

public class CDA extends Application {

    private  String username;

    public static ArrayList<Comment> data;

    //::::::::::::::::::::::::: CACHE :::::::::::::::::::::::::::


    //************************ onCREATE ******************************
    @Override
    public void onCreate() {
        super.onCreate();

        //Preferences

        //Get a Handle to a SharedPreferences

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    //************************ Loading Class **************************
    protected void initSingletons() {

        NetworkServer.initInstance(this);
        MainDrawer.initInstance(this);
    }

    public static ArrayList<Comment> getData() {
        if(data == null || data.isEmpty()){
            Log.v("output", " CDA data empty ");
            Log.v("output", " CDA data empty ");
            Log.v("output", " CDA data empty ");
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(ArrayList<Comment> mData) {
        if(data == null){
            data = mData;
        }
    }

    //************************* GET AND SET **************************
    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    //**********************************************************************************************
}
