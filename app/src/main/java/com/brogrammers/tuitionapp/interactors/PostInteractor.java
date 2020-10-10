package com.brogrammers.tuitionapp.interactors;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.DatabaseHelper;
import com.brogrammers.tuitionapp.beans.Slider;
import com.brogrammers.tuitionapp.listeners.OnMultipleDownloadListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.brogrammers.tuitionapp.Constants.TAG;

public class PostInteractor {
    private static PostInteractor instance;
    private DatabaseHelper databaseHelper;
    private Context context;
    private CollectionReference collSliders;
    private PostInteractor(Context context){
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
        collSliders = databaseHelper.getDb().collection(Constants.DB_SLIDERS);
    }

    public static PostInteractor getInstance(Context context){
        if (instance == null) instance = new PostInteractor(context);
        return instance;
    }

    public void getImageSliders(final OnMultipleDownloadListener<Slider> listener) {
        List<Slider> sliders = new ArrayList<>();
        collSliders.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error==null && value!=null){
                    Log.d(TAG, "getImageSliders: total= "+value.getDocuments().size());
                    sliders.clear();
                    listener.onStarted();
                    if (value.getMetadata().isFromCache()) Log.d(TAG, "getImageSliders-source: cached");
                    else Log.d(TAG, "getImageSliders-source: server");
                    for (DocumentSnapshot ds: value){
                        try{
                            Slider slider = ds.toObject(Slider.class);
                            sliders.add(slider);
                            Log.d(TAG, "getImageSliders: "+slider.getLink());
                        }catch (Exception e){
                            Log.d(TAG, "getImageSliders: persistence exception");
                            e.printStackTrace();
                        }
                    }

                    listener.onDownloaded(sliders);
                    listener.onFinished();

                }else listener.onFinished();
            }
        });
    }

    //all firebase script will be add here.

}
