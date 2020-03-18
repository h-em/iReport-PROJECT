package com.android.ireport.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static void setLocation(String latitude, String longitude, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(Constatnts.LATITUDE, latitude);
        editor.putString(Constatnts.LONGITUDE, longitude);
        editor.apply();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getString(Constatnts.LONGITUDE, Constatnts.DEFAULT_VALUE);
    }

    public static String getLatitude(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getString(Constatnts.LATITUDE, Constatnts.DEFAULT_VALUE);
    }
}
