package com.brogrammers.tutionbd;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

public class DatabaseHelper {
    private static DatabaseHelper instance;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private Context context;
    public DatabaseHelper(Context context){
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context){
        if (instance==null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    public void init(){
        //init all database reference here
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);
        storage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();
    }

    public FirebaseFirestore getDb(){
        return db;
    }
    public FirebaseStorage getStorage(){
        return storage;
    }
    public FirebaseAuth getAuth(){
        return auth;
    }

}
