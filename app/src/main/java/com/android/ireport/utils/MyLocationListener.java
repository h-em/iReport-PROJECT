package com.android.ireport.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



public class MyLocationListener implements LocationListener {
    private static final String TAG = "MyLocationListener";



    private int mLongitude;
    private int mLatitude;

    Context mContext;
    public MyLocationListener(Context context){
        mContext = context;
    }

    @Override
    public void onLocationChanged(Location loc) {

        Toast.makeText(mContext,"Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();

        String longitude = "Longitude: " + loc.getLongitude();
        Log.d(TAG, "onLocationChanged: longitude: " + longitude);
        String latitude = "Latitude: " + loc.getLatitude();
        Log.d(TAG, "onLocationChanged: latitude: "  + latitude);

        mLatitude = Integer.parseInt(latitude);
        mLongitude = Integer.parseInt(longitude);

        /*------- To get city name from coordinates -------- */
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(loc.getLatitude(),
                    loc.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}


    public int getLongitude() {
        return mLongitude;
    }

    public int getLatitude() {
        return mLatitude;
    }
}
