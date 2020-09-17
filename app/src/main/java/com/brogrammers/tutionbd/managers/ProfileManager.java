package com.brogrammers.tutionbd.managers;

import android.content.Context;
import android.graphics.Bitmap;

import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.beans.User;
import com.brogrammers.tutionbd.interactors.ProfileInteractor;
import com.brogrammers.tutionbd.listeners.OnExistListener;
import com.brogrammers.tutionbd.listeners.OnImageUploadListener;
import com.brogrammers.tutionbd.listeners.OnUploadListener;

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

    public void checkUserExitOnDbOrNot(String uerUid, final OnExistListener listener){
        profileInteractor.findUserByUid(uerUid,listener);
    }

}
