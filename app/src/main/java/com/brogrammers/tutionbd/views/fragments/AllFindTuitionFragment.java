package com.brogrammers.tutionbd.views.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.AdsAdapter;
import com.brogrammers.tutionbd.beans.AdInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllFindTuitionFragment extends Fragment {
    private List<AdInfo> ads;
    private CollectionReference collRef = ApplicationHelper.getDatabaseHelper().getDb().collection(Constants.DB_FIND_TUITION);

    private AdsAdapter adsAdapter;
    private RecyclerView recyclerView;
    private Dialog loadingDialog;

    public AllFindTuitionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ads = new ArrayList<>();
        adsAdapter = new AdsAdapter(requireActivity(),ads);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(requireActivity());
        loadingDialog.setCancelable(false);
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

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setAdapter(adsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.show();
        collRef.orderBy("createdTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                        if (ads.size()<=0) showSnackbarMessage("No ads found.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                    }
                });

    }

    private void showSnackbarMessage(String s) {
        Snackbar.make(recyclerView,s, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }
}