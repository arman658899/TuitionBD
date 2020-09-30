package com.brogrammers.tutionbd.views.findtuitionortutoractivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.ApplicationHelper;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.brogrammers.tutionbd.adapters.InformationHighlightAdapter;
import com.brogrammers.tutionbd.adapters.TabAdapter;
import com.brogrammers.tutionbd.beans.HighlightItem;
import com.brogrammers.tutionbd.views.PostForTuitionActivity;
import com.brogrammers.tutionbd.views.PrivacyPolicyActivity;
import com.brogrammers.tutionbd.views.ProfileActivity;
import com.brogrammers.tutionbd.views.loginactivity.LoginActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.YELLOW;

public class FindTuitionOrTutorActivity extends AppCompatActivity implements FindTuitionOrTutorActivityVP.View, NavigationView.OnNavigationItemSelectedListener {
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

    LinearLayout dotsLayout,linearLayoutContainer;
    TextView[] dots;
    int pauseCounter = 0;

    //navigation drawer
    private static DrawerLayout drawerLayout;
    private static NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tuition);

        presenter = new FindTuitionOrTutorActivityPresenter(this,this);

        drawerLayout = findViewById( R.id.drawerlayout);
        navigationView=findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        mToggle=new ActionBarDrawerToggle(FindTuitionOrTutorActivity.this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        highlightItems = new ArrayList<>();
        adapter = new InformationHighlightAdapter(this,highlightItems,presenter);

        loadingDialog = ApplicationHelper.getUtilsHelper().getLoadingDialog(this);
        loadingDialog.setCancelable(false);

        dotsLayout = findViewById(R.id.linearlayout_dots);
        linearLayoutContainer = findViewById(R.id.linearlayout_container);

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

        //menu button
        findViewById(R.id.imageview_menu).setOnClickListener(v -> openDrawer());

        recyclerView = findViewById(R.id.recyclerview_into_collapse);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        Log.d(Constants.TAG, "onCreate: "+AppPreferences.getProfileType(this));
        if (AppPreferences.getProfileType(this) == Constants.PROFILE_FIND_TUTOR_GUARDIAN){
            //post for tutor
            linearLayoutContainer.setVisibility(View.GONE);
        }else{
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    addDots(layoutManager.findFirstVisibleItemPosition());

                }
            });
        }

    }


    private void addDots(int position){
        dots = new TextView [highlightItems.size()];
        dotsLayout.removeAllViews();
        for (int i=0; i<dots.length ; i++){
            dots[i] = new TextView( this );
            if (i == position){
                dots[i].setTextColor( getResources().getColor( R.color.colorWhite ) );
            }
            dots[i].setText( Html.fromHtml("&#8226;") );
            dots[i].setTextSize( 35 );

            dotsLayout.addView( dots[i] );
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
        if (highlightItems.size()>0) addDots(0);
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
    public void onCheckLocationStatus() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (pauseCounter % 5 == 0) {
                    showOnLocationAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPresenter(FindTuitionOrTutorActivityVP.Presenter presenter) {
        //presenter is actually initialize here
        this.presenter = presenter;
    }

    private void showOnLocationAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your location.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        pauseCounter++;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.privacy_policy){
            //privacy policy activity
            closeDrawer();

            Intent intent = new Intent(FindTuitionOrTutorActivity.this, PrivacyPolicyActivity.class);
            startActivity(intent);
        }
        if (id == R.id.profile){
            //profile
            closeDrawer();

            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.logout){
            //logout

            ApplicationHelper.getDatabaseHelper().getAuth().signOut();

            if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser()==null){
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        }

        if (id==R.id.share_app){
            //share app
            closeDrawer();
            Toast.makeText(this, "App Link will be added..", Toast.LENGTH_SHORT).show();


        }
        if (id == R.id.rate_us){
            //reate us
            closeDrawer();
            /*try{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://brotechit.com/home")));
            }catch (Exception e){
                Toast.makeText(this, "Developed by https://brotechit.com/home", Toast.LENGTH_LONG).show();
            }*/
            Toast.makeText(this, "Google Playstore link will be added.", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.about_us){
            //about us
            closeDrawer();
            Toast.makeText(this, "On developing.", Toast.LENGTH_SHORT).show();
        }

        if(id==R.id.developer_option){
            //about developer
            closeDrawer();
            try{
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://brotechit.com/home")));
            }catch (Exception e){
                Toast.makeText(this, "Developed by https://brotechit.com/home", Toast.LENGTH_LONG).show();
            }

        }
        return false;
    }

    private void closeDrawer(){
        drawerLayout.closeDrawer(GravityCompat.START);

    }
    private void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

}
