package com.brogrammers.tuitionapp.managers;

import android.content.Context;
import android.graphics.Bitmap;

import com.brogrammers.tuitionapp.beans.User;
import com.brogrammers.tuitionapp.interactors.ProfileInteractor;
import com.brogrammers.tuitionapp.listeners.OnDataDownloadListener;
import com.brogrammers.tuitionapp.listeners.OnExistListener;
import com.brogrammers.tuitionapp.listeners.OnImageUploadListener;
import com.brogrammers.tuitionapp.listeners.OnUploadListener;

import java.util.HashMap;

public class ProfileManager {
    private Context context;
    private static ProfileManager instance;

    private ProfileInteractor profileInteractor;
    private ProfileManager(Context context){
        this.context = context;
        profileInteractor = ProfileInteractor.getInstance(context);
    }

    public static ProfileManager getInstance(Context context){
        if (instance == null) instance = new ProfileManager(context);
        return instance;
    }

    public String getUserDocumentId(){
        return profileInteractor.getUserDocumentId();
    }

    public void uploadImage(Bitmap bitmap, String userUid, final OnImageUploadListener listener){
        profileInteractor.uploadProfileImage(bitmap,userUid,listener);
    }

    public void createUser(User user, final OnUploadListener listener){
        profileInteractor.createUser(user,listener);
    }

    public void createUserHashMap(final HashMap<String,Object> user, final OnUploadListener listener){
        profileInteractor.createUserHashMap(user,listener);
    }

    public void checkUserExitOnDbOrNot(String uerUid, final OnExistListener listener){
        profileInteractor.findUserByUid(uerUid,listener);
    }

    public void getSingleUserDataSnapshot(String uid, final OnDataDownloadListener<User> listener){
        profileInteractor.getSingleUserDataSnapshot(uid,listener);
    }

    public void updateUserChildValue(String documentId, String childKey, String value, final OnUploadListener listener){
        profileInteractor.updateUserChildValue(documentId,childKey,value,listener);
    }

}
