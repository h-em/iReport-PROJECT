package com.android.ireport.utils;

import android.Manifest;


public class Permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA
    };
}
