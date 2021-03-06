package com.brogrammers.tuitionapp.views.findtuitionortutoractivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.brogrammers.tuitionapp.AppPreferences;
import com.brogrammers.tuitionapp.ApplicationHelper;
import com.brogrammers.tuitionapp.beans.HighlightItem;
import com.brogrammers.tuitionapp.beans.Slider;
import com.brogrammers.tuitionapp.beans.User;
import com.brogrammers.tuitionapp.listeners.OnDataDownloadListener;
import com.brogrammers.tuitionapp.listeners.OnMultipleDownloadListener;
import com.brogrammers.tuitionapp.managers.PostManager;
import com.brogrammers.tuitionapp.managers.ProfileManager;
import com.brogrammers.tuitionapp.views.PostForTuitionActivity;
import com.brogrammers.tuitionapp.views.loginactivity.LoginActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.brogrammers.tuitionapp.Constants.TAG;

public class FindTuitionOrTutorActivityPresenter implements FindTuitionOrTutorActivityVP.Presenter {
    private Context context;
    private PostManager postManager;
    private ProfileManager profileManager;
    private FindTuitionOrTutorActivityVP.View view;
    private List<HighlightItem> highlightItems;
    private List<Slider> sliderList;

    public FindTuitionOrTutorActivityPresenter(Context context, FindTuitionOrTutorActivityVP.View view) {
        this.view = view;
        this.context = context;
        postManager = PostManager.getInstance(context);
        profileManager = ProfileManager.getInstance(context);
        //post manager should add before setPresenter.
        this.view.setPresenter(this);
        highlightItems = new ArrayList<>();
        sliderList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        //show all item
        if (ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser() == null) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.navigateToActivity(intent);
        } else {

            profileManager.getSingleUserDataSnapshot(ApplicationHelper.getDatabaseHelper().getAuth().getCurrentUser().getUid(), new OnDataDownloadListener<User>() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onDownloading(User user) {
                    AppPreferences.UserInfo.setUserInfo(context, user);
                }

                @Override
                public void onError() {

                }
            });

            postManager.getImageSlider(new OnMultipleDownloadListener<Slider>() {
                @Override
                public void onStarted() {
                    sliderList.clear();

                }

                @Override
                public void onFinished() {
                    view.onInitializeViewFlipper(sliderList);
                    Log.d(TAG, "sliders size = "+sliderList.size());
                }

                @Override
                public void onDownloaded(List<Slider> list) {
                    sliderList.addAll(list);
                }

                @Override
                public void onError() {

                }
            });

            view.onSetupProfileUi();
        }

        /*highlightItems.clear();

        if (AppPreferences.UserInfo.getUserCollete(context).isEmpty()) {
            highlightItems.add(new HighlightItem("Add your University/College", "Please add your educational institute information for better response from Guardians.", "college", "ADD"));
        } else
            highlightItems.add(new HighlightItem("Update University/College", AppPreferences.UserInfo.getUserCollete(context) + " is already added.", "college", "UPDATE"));

        if (AppPreferences.UserInfo.getUserSubject(context).isEmpty()) {
            highlightItems.add(new HighlightItem("Add your Dept./Subject", "Please add your Department/Subject name for better response from Guardians.", "subject", "ADD"));
        } else
            highlightItems.add(new HighlightItem("Update Dept./Subject", AppPreferences.UserInfo.getUserSubject(context) + " is already added.", "subject", "UPDATE"));

        if (AppPreferences.UserInfo.getUserStudentIDorNID(context).isEmpty()) {
            highlightItems.add(new HighlightItem("Add STUDENT_ID/NID", "Add your student ID or NID card's photo for authentication.", "idCardLink", "ADD"));
        } else
            highlightItems.add(new HighlightItem("Update STUDENT_ID/NID", "You have already added your Identification card.", "idCardLink", "UPDATE"));

        view.onShowHighlightedItems(highlightItems);*/

        initializeLocationSetup();

    }

    @Override
    public void onPostButtonClick() {
        Intent intent = new Intent(context, PostForTuitionActivity.class);
        view.navigateToActivity(intent);
    }


    private void initializeLocationSetup() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions((AppCompatActivity) context,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 111);
        }
    }

    private void getCurrentLocation() {

       view.onCheckLocationStatus();

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(900000);
        locationRequest.setFastestInterval(20000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        //only once
                        LocationServices.getFusedLocationProviderClient(context)
                                .removeLocationUpdates(this);

                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            try {
                                int index = locationResult.getLocations().size() - 1;
                                double longitude = locationResult.getLocations().get(index).getLongitude();
                                double latitude = locationResult.getLocations().get(index).getLatitude();

                                Location location = new Location("providerNA");
                                location.setLatitude(latitude);
                                location.setLongitude(longitude);
                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        1
                                );

                                if (addresses != null && addresses.get(0) != null) {
                                    longitude = addresses.get(0).getLongitude();
                                    latitude = addresses.get(0).getLatitude();

                                    AppPreferences.setUserLocation(context,latitude,longitude);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, Looper.getMainLooper());








        /*mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(
                                MainActivity.this,
                                Locale.getDefault()
                        );
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),
                                location.getLongitude(),
                                1
                        );

                        double longitude = addresses.get(0).getLongitude();
                        double latitude = addresses.get(0).getLatitude();
                        String locality = addresses.get(0).getLocality();

                        updateUserLocation(longitude,latitude,locality);

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

}
