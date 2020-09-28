package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.InformationHighlightAdapter;
import com.brogrammers.tutionbd.adapters.TabAdapter;
import com.brogrammers.tutionbd.views.PostForTuitionActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class FindTuitionOrTutorActivity extends AppCompatActivity implements FindTuitionOrTutorActivityVP.View{
    private FindTuitionOrTutorActivityVP.Presenter presenter;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;
    private TabLayout tab;
    private RecyclerView recyclerView;
    private TextView tvPostHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tuition);

        presenter = new FindTuitionOrTutorActivityPresenter(this,this);

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
        recyclerView.setAdapter(new InformationHighlightAdapter(this));

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

    }

    @Override
    public void navigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void setPresenter(FindTuitionOrTutorActivityVP.Presenter presenter) {
        //presenter is actually initialize here
        this.presenter = presenter;
    }
}
