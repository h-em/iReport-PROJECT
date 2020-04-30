package com.android.ireport.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.android.ireport.model.Report;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Utils {
    private static final String TAG = "Utils";

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
        return pref.getString(Constatnts.LONGITUDE, Constatnts.DEFAULT_VALUE_LOCATION);
    }

    public static String getLatitude(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getString(Constatnts.LATITUDE, Constatnts.DEFAULT_VALUE_LOCATION);
    }

    public static Boolean getStatus(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getBoolean(Constatnts.DELETED, false);
    }

    public static void setStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        editor.putBoolean(Constatnts.DELETED, status);
        editor.apply();
    }


    public static void setReportsList(Context context, List<Report> reports){
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(reports);
        Log.d(TAG, "onDataChange: json: " + json);
        prefsEditor.putString(Constatnts.REPORT_LIST_KEY, json);
        prefsEditor.apply();
    }

    public static List<Report> getReportsList(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(Constatnts.PRIVATE_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(Constatnts.REPORT_LIST_KEY, Constatnts.DEFAULT_VALUE);
        Type type = new TypeToken<List<Report>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
