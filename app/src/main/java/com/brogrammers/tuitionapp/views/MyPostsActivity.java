package com.brogrammers.tuitionapp.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.adapters.AdsAdapter;
import com.brogrammers.tuitionapp.adapters.MyPostsAdapter;
import com.brogrammers.tuitionapp.beans.AdInfo;
import com.brogrammers.tuitionapp.listeners.OnMyPostClickListener;
import com.brogrammers.tuitionapp.listeners.OnRecyclerViewItemClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.brogrammers.tuitionapp.Constants.TAG;

public class MyPostsActivity extends AppCompatActivity implements OnMyPostClickListener<AdInfo> {
    private List<AdInfo> ads;
    private Toolbar toolbar;
    private CollectionReference collRef,locationRef;
    private MyPostsAdapter adsAdapter;
    private RecyclerView recyclerView;
    private TextView tvNoPostFound;
    private Dialog loadingDialog;

    long lastDownloadedAd = Calendar.getInstance().getTimeInMillis();
    ListenerRegistration listenerRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        init();
        initUI();

    }

    private void init() {
        ads = new ArrayList<>();
        adsAdapter = new MyPostsAdapter(this,ads,this);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        switch (AppPreferences.getProfileType(MyPostsActivity.this)){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_LOCATION);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_LOCATION);
                break;

            }
            default:
        }


    }

    private void initUI() {
        tvNoPostFound = findViewById(R.id.textview_no_post_found);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Posts");
        toolbar.setNavigationIcon(R.drawable.icon_back_arrow);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adsAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    if (ads.size()-1 == layoutManager.findLastCompletelyVisibleItemPosition()){
                        loadMoreAdsFromServer();
                    }
                }
            }
        });

    }

    private void loadMoreAdsFromServer() {
        loadingDialog.show();
        collRef.orderBy("createdTime", Query.Direction.DESCENDING)
                .whereLessThanOrEqualTo("createdTime",lastDownloadedAd-1)
                .whereEqualTo("userUid",ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid())
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot ds: queryDocumentSnapshots){
                            try{
                                AdInfo adInfo = ds.toObject(AdInfo.class);
                                lastDownloadedAd = adInfo.getCreatedTime();
                                ads.add(adInfo);
                            }catch (Exception e){
                                Log.d(TAG, "loadmore persisting error: ");
                                e.printStackTrace();
                            }
                        }
                        loadingDialog.dismiss();
                        adsAdapter.notifyDataSetChanged();
                        Log.d(TAG, "getAllAds downloaded total: "+ads.size());
                        /*if (ads.size()<=0) {
                            getAdDataFromServer();
                        }*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                    }
                });
    }

    private void getAdDataFromServer() {
        if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
            Toast.makeText(this, "You are not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingDialog.show();
        listenerRegistration = collRef
                .orderBy("createdTime", Query.Direction.DESCENDING)
                .whereEqualTo("userUid",ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid())
                .limit(20)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null && error==null){
                            String source = value.getMetadata().isFromCache() ? "From Cache":"From Server";
                            Log.d(TAG, "getAllAds source: "+source);

                            ads.clear();
                            for (DocumentSnapshot ds: value){
                                try{
                                    AdInfo adInfo = ds.toObject(AdInfo.class);
                                    lastDownloadedAd = adInfo.getCreatedTime();

                                    ads.add(adInfo);
                                }catch (Exception e){
                                    Log.d(TAG, "getAllAds persisting error: ");
                                    e.printStackTrace();
                                }
                            }
                            Log.d(TAG, "getAllAds downloaded total: "+ads.size());



                        }

                        loadingDialog.dismiss();
                        adsAdapter.notifyDataSetChanged();
                        if (ads.size()<=0){
                            tvNoPostFound.setVisibility(View.VISIBLE);
                        }else tvNoPostFound.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onItemSelected(AdInfo adInfo) {
        switch (AppPreferences.getProfileType(MyPostsActivity.this)){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                //show post for teacher
                Intent intent = new Intent(MyPostsActivity.this, ShowGuardianDetailsActivity.class);

                /*Bundle bundle = new Bundle();
                bundle.putString("tittle",adInfo.getTittle());
                bundle.putString("salary",adInfo.getSalary());
                bundle.putString("location",adInfo.getLocation());
                bundle.putString("subject",adInfo.getSubject());
                bundle.putString("class",adInfo.getStudentClass());
                bundle.putString("language",adInfo.getLanguage());
                bundle.putString("schedule",adInfo.getSchedule());
                bundle.putString("documentId",adInfo.getDocumentId());
                bundle.putString("userUid",adInfo.getUserUid());
                bundle.putLong("time",adInfo.getCreatedTime());*/

                intent.putExtra("ad",adInfo);
                startActivity(intent);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                //show post for guardian


                Intent intent = new Intent(MyPostsActivity.this, ShowTeacherDetailsActivity.class);
                /*Bundle bundle = new Bundle();
                bundle.putString("tittle",adInfo.getTittle());
                bundle.putString("salary",adInfo.getSalary());
                bundle.putString("location",adInfo.getLocation());
                bundle.putString("subject",adInfo.getSubject());
                bundle.putString("class",adInfo.getStudentClass());
                bundle.putString("language",adInfo.getLanguage());
                bundle.putString("schedule",adInfo.getSchedule());
                bundle.putString("documentId",adInfo.getDocumentId());
                bundle.putString("userUid",adInfo.getUserUid());
                bundle.putLong("time",adInfo.getCreatedTime());

                intent.putExtras(bundle);*/

                intent.putExtra("ad",adInfo);


                startActivity(intent);
                break;
            }
            default:
        }
    }

    @Override
    public void onDelete(AdInfo adInfo) {
        showDeleteConfirmation(adInfo);
    }


    @Override
    public void onUpdate(AdInfo adInfo) {
        Intent intent = new Intent(MyPostsActivity.this,PostForTuitionActivity.class);
        intent.putExtra("ad",adInfo);
        intent.putExtra("update",true);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        loadingDialog.show();
        //getDataFromCache();
        getAdDataFromServer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (listenerRegistration!=null) listenerRegistration.remove();
    }

    private void showDeleteConfirmation(AdInfo adInfo) {
        AlertDialog.Builder  builder = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to delete this post?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        loadingDialog.show();
                        collRef.document(adInfo.getDocumentId())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingDialog.dismiss();
                                        if (task.isSuccessful()){
                                            Toast.makeText(MyPostsActivity.this, "Post is deleted.", Toast.LENGTH_SHORT).show();
                                            try{
                                                ads.remove(adInfo);
                                                adsAdapter.notifyDataSetChanged();
                                                locationRef.document(adInfo.getDocumentId()).delete();
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }else{
                                            Toast.makeText(MyPostsActivity.this, "Failed to delete post. Please try again later.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}