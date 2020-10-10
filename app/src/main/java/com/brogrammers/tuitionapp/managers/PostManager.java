package com.brogrammers.tuitionapp.managers;

import android.content.Context;

import com.brogrammers.tuitionapp.beans.Slider;
import com.brogrammers.tuitionapp.interactors.PostInteractor;
import com.brogrammers.tuitionapp.listeners.OnMultipleDownloadListener;

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

    public void getImageSlider(final OnMultipleDownloadListener<Slider> listener){
        postInteractor.getImageSliders(listener);
    }

}
