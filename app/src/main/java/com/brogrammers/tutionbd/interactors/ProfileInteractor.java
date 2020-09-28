package com.brogrammers.tutionbd.interactors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.DatabaseHelper;
import com.brogrammers.tutionbd.beans.User;
import com.brogrammers.tutionbd.enums.DbEnums;
import com.brogrammers.tutionbd.listeners.OnDataDownloadListener;
import com.brogrammers.tutionbd.listeners.OnExistListener;
import com.brogrammers.tutionbd.listeners.OnImageUploadListener;
import com.brogrammers.tutionbd.listeners.OnUploadListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ProfileInteractor {
    private Context context;
    private static ProfileInteractor instance;

    private DatabaseHelper databaseHelper;
    private CollectionReference collUsers;
    private StorageReference storageUsersPic;
    private ProfileInteractor(Context context){
        this.context = context;
        databaseHelper = ApplicationHelper.getDatabaseHelper();
        collUsers = databaseHelper.getDb().collection(Constants.DB_USERS);
        storageUsersPic = databaseHelper.getStorage().getReference().child(Constants.STORAGE_USERS_PIC);
    }

    public static ProfileInteractor getInstance(Context context){
        if (instance == null){
            instance = new ProfileInteractor(context);
        }
        return instance;
    }

    public String getUserDocumentId(){
        return collUsers.document().getId();
    }

    public void createUser(User user, final OnUploadListener listener){
        /*String documentId = getUserDocumentId();
        if (documentId == null) {
            listener.onFailed();
            return;
        }
        user.setDocumentId(documentId);*/
        collUsers.document(user.getDocumentId())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) listener.onUploaded();
                        else listener.onFailed();
                    }
                });

    }

    public void findUserByUid(String userUid, final OnExistListener listener){
        collUsers.whereEqualTo("userUid",userUid)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            listener.onExist(false);
                        }else listener.onExist(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onError();
                    }
                });
    }

    public void uploadProfileImage(Bitmap bitmap,String userUid, final OnImageUploadListener listener){
        final StorageReference imageName = storageUsersPic.child(userUid);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] data = baos.toByteArray();

        imageName.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.onUploaded(String.valueOf(uri));
                        }
                    });
                }else{
                    listener.onUploaded("");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUploaded("");
            }
        });
    }

    public void getSingleUserDataSnapshot(String uid, final OnDataDownloadListener<User> listener){
        collUsers.whereEqualTo("uid",uid)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error==null){
                            listener.onStarted();
                            for (DocumentSnapshot ds: value){
                                try {
                                    User user = ds.toObject(User.class);
                                    listener.onDownloading(user);
                                }catch (Exception e){
                                    listener.onError();
                                    e.printStackTrace();
                                }
                            }
                            listener.onFinished();
                        }else{
                            listener.onError();
                        }
                    }
                });
    }

    public void updateUserChildValue(String documentId,String childKey, String value, final OnUploadListener listener){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put(childKey,value);
        collUsers.document(documentId)
                .set(hashMap, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) listener.onUploaded();
                        else listener.onFailed();
                    }
                });
    }


}
