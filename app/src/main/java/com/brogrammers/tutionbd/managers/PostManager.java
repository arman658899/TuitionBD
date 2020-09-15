package com.brogrammers.tutionbd.managers;

import android.content.Context;

import com.brogrammers.tutionbd.interactors.PostInteractor;
import com.google.firebase.firestore.ListenerRegistration;

public class PostManager extends FirebaseListenerManager{
    private static PostManager instance;
    private PostInteractor postInteractor;

    private Context context;
    private PostManager(Context context){
        this.context = context;
        postInteractor = PostInteractor.getInstance(context);
    }

    public static PostManager getInstance(Context context){
        if (instance==null) instance = new PostManager(context);
        return instance;
    }

}
