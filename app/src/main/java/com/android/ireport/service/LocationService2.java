package com.android.ireport.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationService2 {
    private static final String TAG = "LocationService";

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    public LocationService2(Context context){
        mContext = context;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext);

    }




/*
    private void fetchLastLocation() {
        Log.d(TAG, "fetchLastLocation");
        Task<Location> task = mFusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //mCurrentLocation = location;

                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d(TAG, "onLocationResult: " + latitude + " " + longitude);
                    Toast.makeText(mContext, longitude + " " + longitude + "", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }*/

    private void requestLocation() {
        LocationRequest locationRequest = new LocationRequest();
        //locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.d(TAG, "onLocationResult: " + latitude + " " + longitude);
                Toast.makeText(mContext, longitude + " " + longitude + "", Toast.LENGTH_SHORT).show();
            }

        };

        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
    }

}
