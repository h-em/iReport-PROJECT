package com.android.ireport.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.ireport.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationService extends Service {
    private static final String TAG = "LocationService";

    private Context mContext;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: service is created");
        mContext = LocationService.this;
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand.");
        requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

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

                Utils.setLocation(Double.toString(latitude), Double.toString(longitude), mContext);

                Log.d(TAG, "onLocationResult: " + latitude + " " + longitude);
                Toast.makeText(mContext, longitude + " " + longitude + "", Toast.LENGTH_SHORT).show();
            }

        };

        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, null);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy locationService.");
        super.onDestroy();
    }

}
