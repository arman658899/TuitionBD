package com.brogrammers.tutionbd.views.locationactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.brogrammers.tutionbd.AppPreferences;
import com.brogrammers.tutionbd.Constants;
import com.brogrammers.tutionbd.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient providerClient;
    private GoogleMap mMap;
    private Location mLocation;
    private boolean locationPermissionGranted = false;

    private String mAddress = "";
    private double mLat, mLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        mLat = AppPreferences.getUserLatitude(this);
        mLon = AppPreferences.getUserLongitude(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        providerClient = LocationServices.getFusedLocationProviderClient(this);

        //getLocationPermission();

        //select current location
        findViewById(R.id.button_select_current_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
            }
        });


    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            //setUpCurrentLocationToMap();
            getDeviceLocation();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    // setUpCurrentLocationToMap();
                    getDeviceLocation();
                }
                break;
            }
        }
    }

    private void setUpCurrentLocationToMap() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = providerClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mLocation = location;

                        }
                    });
                }
            }
        });
    }

    private void getDeviceLocation() {
        /*LatLng latLng = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("You are here.");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        mMap.addMarker(options);*/

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = providerClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLocation = task.getResult();
                            if (mLocation != null) {

                                mLat = mLocation.getLatitude();
                                mLon = mLocation.getLongitude();

                                AppPreferences.setUserLocation(SelectLocationActivity.this, mLat, mLon);

                                Geocoder geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(
                                            mLat,
                                            mLon,
                                            1
                                    );

                                    if (addresses != null && addresses.get(0) != null) {
                                        /*longitude = addresses.get(0).getLongitude();
                                        latitude = addresses.get(0).getLatitude();*/

                                        mAddress = "" + addresses.get(0).getAddressLine(0);
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLat, mLon), 10));
                                MarkerOptions options = new MarkerOptions()
                                        .position(new LatLng(mLat, mLon))
                                        .title(mAddress);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((new LatLng(mLat, mLon)), 15f));
                                mMap.addMarker(options);

                                showConfirmDialog(mAddress);

                            }
                        } else {
                            Log.d(Constants.TAG, "Current location is null. Using defaults.");
                            Log.e(Constants.TAG, "Exception: %s", task.getException());

                            LatLng latLng = new LatLng(mLat, mLon);

                            Geocoder geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
                            List<Address> addresses = null;
                            try {
                                addresses = geocoder.getFromLocation(
                                        mLat,
                                        mLon,
                                        1
                                );

                                if (addresses != null && addresses.get(0) != null) {
                                        /*longitude = addresses.get(0).getLongitude();
                                        latitude = addresses.get(0).getLatitude();*/

                                    mAddress = "" + addresses.get(0).getAddressLine(0);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 15f));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);


                            MarkerOptions options = new MarkerOptions()
                                    .position(latLng)
                                    .title(mAddress);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                            mMap.addMarker(options);

                            showConfirmDialog(mAddress);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }

    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showOnLocationAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);*/

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();

                mLat = latLng.latitude;
                mLon = latLng.longitude;

                Geocoder geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(
                            mLat,
                            mLon,
                            1
                    );

                    if (addresses!=null &&addresses.get(0)!=null){
                                        /*longitude = addresses.get(0).getLongitude();
                                        latitude = addresses.get(0).getLatitude();*/

                        mAddress = ""+addresses.get(0).getAddressLine(0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLat, mLon), 5));
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title(mAddress);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
                mMap.addMarker(options);

                showConfirmDialog(mAddress);
            }
        });

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(latLng.latitude,
                                latLng.longitude), 10));
                MarkerOptions options = new MarkerOptions()
                        .position(new LatLng(latLng.latitude, latLng.longitude))
                        .title("You are here.");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                mMap.addMarker(options);

            }
        });*/


        updateLocationUI();
        getDeviceLocation();


    }

    private void showConfirmDialog(String address){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to select "+address+" for your post location?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("address",address);
                        returnIntent.putExtra("lat",mLat);
                        returnIntent.putExtra("lon",mLon);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        statusCheck();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (mAddress.isEmpty()){
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED,returnIntent);
            finish();
        }else{
            showConfirmDialog(mAddress);
        }
    }
}