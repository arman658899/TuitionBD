package com.brogrammers.tutionbd.views.fragments;

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

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.AdsAdapter;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.brogrammers.tutionbd.listeners.OnRecyclerViewItemClickListener;
import com.brogrammers.tutionbd.views.ShowTeacherDetailsActivity;
import com.brogrammers.tutionbd.views.ShowGuardianDetailsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;
import static com.brogrammers.tutionbd.Constants.TAG;

public class AllFindTuitionFragment extends Fragment implements OnRecyclerViewItemClickListener<AdInfo> {
    private List<AdInfo> ads;
    private CollectionReference collRef;
    private AdView mAdView;
    private AdsAdapter adsAdapter;
    private RecyclerView recyclerView;
    private TextView tvNoPostFound;
    private Dialog loadingDialog;

    public AllFindTuitionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ads = new ArrayList<>();
        adsAdapter = new AdsAdapter(requireActivity(),ads,this);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(requireActivity());
        loadingDialog.setCancelable(false);

        switch (AppPreferences.getProfileType(requireActivity())){
            case Constants.PROFILE_FIND_TUITION_TEACHER:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUTOR_TO_TEACHER);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION_TO_GUARDIAN);
                break;

            }
            default:
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_find_tuition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //test ad
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        tvNoPostFound = view.findViewById(R.id.textview_no_post_found);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        loadingDialog.show();
        getDataFromCache();

    }

    private void getDataFromCache() {
        collRef.orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(20)
                .get(Source.CACHE)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String source = queryDocumentSnapshots.getMetadata().isFromCache() ? "From Cache":"From Server";
                        Log.d(TAG, "getAllAds source: "+source);

                        ads.clear();
                        for (DocumentSnapshot ds: queryDocumentSnapshots){
                            try{
                                AdInfo adInfo = ds.toObject(AdInfo.class);
                                ads.add(adInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        loadingDialog.dismiss();
                        adsAdapter.notifyDataSetChanged();
                        if (ads.size()<=0) {
                           getAdDataFromServer();
                        }
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
        loadingDialog.show();
        collRef.orderBy("createdTime", Query.Direction.DESCENDING)
                .limit(20)
                .get(Source.SERVER)
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String source = queryDocumentSnapshots.getMetadata().isFromCache() ? "From Cache":"From Server";
                        Log.d(TAG, "getAllAds source: "+source);

                        ads.clear();
                        for (DocumentSnapshot ds: queryDocumentSnapshots){
                            try{
                                AdInfo adInfo = ds.toObject(AdInfo.class);
                                ads.add(adInfo);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        loadingDialog.dismiss();
                        adsAdapter.notifyDataSetChanged();
                        if (ads.size()<=0) {
                            getAdDataFromServer();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();


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

                Bundle bundle = new Bundle();
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

                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                //show post for guardian


                Intent intent = new Intent(requireActivity(), ShowTeacherDetailsActivity.class);
                Bundle bundle = new Bundle();
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

                intent.putExtras(bundle);

                startActivity(intent);
                break;
            }
            default:
        }
    }
}
