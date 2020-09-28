package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.InformationHighlightAdapter;
import com.brogrammers.tutionbd.adapters.TabAdapter;
import com.brogrammers.tutionbd.beans.HighlightItem;
import com.brogrammers.tutionbd.beans.User;
import com.brogrammers.tutionbd.views.PostForTuitionActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class FindTuitionOrTutorActivity extends AppCompatActivity implements FindTuitionOrTutorActivityVP.View{
    private FindTuitionOrTutorActivityVP.Presenter presenter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;
    private TabLayout tab;
    private RecyclerView recyclerView;
    private InformationHighlightAdapter adapter;
    private List<HighlightItem> highlightItems;
    private AlertDialog dialogBoxForUpdate;
    private TextView tvPostHere;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tuition);

        presenter = new FindTuitionOrTutorActivityPresenter(this,this);

        highlightItems = new ArrayList<>();
        adapter = new InformationHighlightAdapter(this,highlightItems,presenter);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        tvPostHere = findViewById(R.id.textview_post_here);
        tvPostHere.setOnClickListener(v -> presenter.onPostButtonClick());

        viewPager = findViewById(R.id.viewpager);
        tab = findViewById(R.id.tablayout);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setVisibility(View.GONE);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
        tab.setupWithViewPager(viewPager);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.textview_post_here).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindTuitionOrTutorActivity.this, PostForTuitionActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerview_into_collapse);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        recyclerView.setAdapter(adapter);


        Log.d(Constants.TAG, "onCreate: "+AppPreferences.getProfileType(this));
        if (AppPreferences.getProfileType(this) == Constants.PROFILE_FIND_TUTOR_GUARDIAN){
            //post for tutor
            recyclerView.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onStart();
    }


    @Override
    public void onSetupProfileUi() {
        if (AppPreferences.getProfileType(this)== Constants.PROFILE_FIND_TUITION_TEACHER){
            //post for tuition
            tvPostHere.setText("Need Tuition");
        }else if (AppPreferences.getProfileType(this)==Constants.PROFILE_FIND_TUTOR_GUARDIAN){
            //post for tutor
            tvPostHere.setText("Need Tutor");
        }
    }

    @Override
    public void showSnackBarMessage(String message) {
        Snackbar.make(recyclerView,message, BaseTransientBottomBar.LENGTH_LONG)
                .setBackgroundTint(YELLOW)
                .setTextColor(BLACK)
                .show();
    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onShowHighlightedItems(List<HighlightItem> items) {
        highlightItems.clear();
        highlightItems.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onShowUpdateDialogBox(String tittle, String buttonName, String childKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FindTuitionOrTutorActivity.this);
        View itemView = getLayoutInflater().inflate(R.layout.diaglogview_update_data,null,false);

        TextView tvTittle = itemView.findViewById(R.id.textview_tittle);
        EditText etInfo = itemView.findViewById(R.id.textview_details);
        Button buttonCancel = itemView.findViewById(R.id.button_cancel);
        Button buttonAddOrUpdate = itemView.findViewById(R.id.button_add);

        buttonAddOrUpdate.setText(buttonName);
        tvTittle.setText(tittle);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogBoxForUpdate!=null) dialogBoxForUpdate.cancel();
            }
        });

        buttonAddOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddOrUpdateHighlightedItem(""+etInfo.getText().toString(),childKey);
                if (dialogBoxForUpdate!=null) dialogBoxForUpdate.cancel();
            }
        });

        builder.setView(itemView);

        dialogBoxForUpdate = builder.create();
        dialogBoxForUpdate.show();

    }

    @Override
    public void onLoadingDialog(boolean isLoading) {
        if (isLoading) loadingDialog.show();
        else loadingDialog.dismiss();
    }

    @Override
    public void setPresenter(FindTuitionOrTutorActivityVP.Presenter presenter) {
        //presenter is actually initialize here
        this.presenter = presenter;
    }

}
