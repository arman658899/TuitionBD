package com.brogrammers.tuitionapp.views.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.Constants;
import com.brogrammers.tuitionapp.R;
import com.brogrammers.tuitionapp.adapters.AdsAdapter;
import com.brogrammers.tuitionapp.beans.AdInfo;
import com.brogrammers.tuitionapp.listeners.OnRecyclerViewItemClickListener;
import com.brogrammers.tuitionapp.views.ShowTeacherDetailsActivity;
import com.brogrammers.tuitionapp.views.ShowGuardianDetailsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.listeners.GeoQueryEventListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;
import static com.brogrammers.tuitionapp.Constants.TAG;

public class NearestFindTuition extends Fragment implements OnRecyclerViewItemClickListener<AdInfo> {
    private TextView tvNoPostFound;
    private RecyclerView recyclerView;
    private AdsAdapter adsAdapter;
    private List<AdInfo> ads;
    private Dialog loadingDialog;
    private Map<String,String> mappedKey;

    private CollectionReference collRef,locationRef;
    private GeoFirestore geoFirestore;

    private List<String> nearestPostIds;


    public NearestFindTuition() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ads = new ArrayList<>();
        mappedKey = new HashMap<>();
        nearestPostIds = new ArrayList<>();
        adsAdapter = new AdsAdapter(requireActivity(),ads,this);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(requireActivity());
        loadingDialog.setCancelable(false);

        switch (AppPreferences.getProfileType(requireActivity())){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_LOCATION);
                geoFirestore = new GeoFirestore(locationRef);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                locationRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_LOCATION);
                geoFirestore = new GeoFirestore(locationRef);
                break;

            }
            default:
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearest_find_tuition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        tvNoPostFound = view.findViewById(R.id.textview_no_post_found);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        loadingDialog.show();
        nearestPostIds.clear();
        //AppPreferences.getUserLatitude(requireActivity())
        //AppPreferences.getUserLongitude(requireActivity()

        GeoPoint point = new GeoPoint(
                AppPreferences.getUserLatitude(requireActivity()),
                AppPreferences.getUserLongitude(requireActivity())
        );
       /* geoFirestore.getAtLocation(point, 100000.0, new GeoFirestore.SingleGeoQueryDataEventCallback() {
            @Override
            public void onComplete(@org.jetbrains.annotations.Nullable List<? extends DocumentSnapshot> list, @org.jetbrains.annotations.Nullable Exception e) {
                if (e != null){
                    loadingDialog.dismiss();
                    Log.d(TAG, "onComplete: list null or error: ");
                    showSnackbarMessage("No post found.");

                }else{
                    Log.d(TAG, "onComplete: "+list.size());
                    loadingDialog.dismiss();
                    for (DocumentSnapshot ds: list){
                        Log.d(TAG, "onComplete: "+ds.getReference());
                    }
                }
            }
        });*/

       geoFirestore.queryAtLocation(point,10.10)
               .addGeoQueryEventListener(new GeoQueryEventListener() {
                   @Override
                   public void onKeyEntered(@NotNull String s, @NotNull GeoPoint geoPoint) {
                       if (!mappedKey.containsValue(s)){
                           nearestPostIds.add(s);
                           mappedKey.put(s,s);
                       }
                   }

                   @Override
                   public void onKeyExited(@NotNull String s) {

                   }

                   @Override
                   public void onKeyMoved(@NotNull String s, @NotNull GeoPoint geoPoint) {

                   }

                   @Override
                   public void onGeoQueryReady() {
                       loadingDialog.dismiss();
                       Log.d(TAG, "onGeoQueryReady: "+nearestPostIds.size());
                       for (String str: nearestPostIds){
                           Log.d(TAG, "doctId: "+str);

                           collRef.whereEqualTo("documentId",str)
                                   .get()
                                   .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                       @Override
                                       public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                           if (queryDocumentSnapshots!=null){
                                               for (DocumentSnapshot ds: queryDocumentSnapshots){
                                                   try{
                                                       AdInfo info = ds.toObject(AdInfo.class);
                                                       ads.add(info);
                                                   }catch (Exception e){
                                                       e.printStackTrace();
                                                   }
                                               }
                                               adsAdapter.notifyDataSetChanged();
                                           }
                                       }
                                   });

                       }
                   }

                   @Override
                   public void onGeoQueryError(@NotNull Exception e) {

                   }
               });


    }

    private void showSnackbarMessage(String s) {
        Snackbar.make(recyclerView,s, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }

    @Override
    public void onItemSelected(AdInfo adInfo) {
        switch (AppPreferences.getProfileType(requireActivity())){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                //show post for teacher
                Intent intent = new Intent(requireActivity(), ShowGuardianDetailsActivity.class);
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
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                //show post for guardian
                Intent intent = new Intent(requireActivity(), ShowTeacherDetailsActivity.class);
               /* Bundle bundle = new Bundle();
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
}
