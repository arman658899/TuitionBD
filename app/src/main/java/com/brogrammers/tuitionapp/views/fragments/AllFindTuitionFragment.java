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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;
import static com.brogrammers.tuitionapp.Constants.TAG;

public class AllFindTuitionFragment extends Fragment implements OnRecyclerViewItemClickListener<AdInfo> {
    private List<AdInfo> ads;
    private CollectionReference collRef;
    private AdsAdapter adsAdapter;
    private RecyclerView recyclerView;
    private TextView tvNoPostFound;
    private Dialog loadingDialog;

    long lastDownloadedAd = Calendar.getInstance().getTimeInMillis();
    ListenerRegistration listenerRegistration;

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



        tvNoPostFound = view.findViewById(R.id.textview_no_post_found);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
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

    @Override
    public void onStart() {
        super.onStart();



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
                                lastDownloadedAd = adInfo.getCreatedTime();
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
        listenerRegistration = collRef.orderBy("createdTime", Query.Direction.DESCENDING)
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

                            loadingDialog.dismiss();
                            adsAdapter.notifyDataSetChanged();
                        }loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onResume() {
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
                bundle.putLong("time",adInfo.getCreatedTime());*/

                intent.putExtra("ad",adInfo);
                startActivity(intent);
                break;
            }
            case Constants.PROFILE_FIND_TUTOR_GUARDIAN:{
                //show post for guardian


                Intent intent = new Intent(requireActivity(), ShowTeacherDetailsActivity.class);
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
}
