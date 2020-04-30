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
        return string.equals("");
    }

    public static void setLocation(String latitude, String longitude, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        editor.putString(Constants.LATITUDE, latitude);
        editor.putString(Constants.LONGITUDE, longitude);
        editor.apply();
    }

    public static String getLongitude(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getString(Constants.LONGITUDE, Constants.DEFAULT_VALUE_LOCATION);
    }

    public static String getLatitude(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getString(Constants.LATITUDE, Constants.DEFAULT_VALUE_LOCATION);
    }

    public static Boolean getStatus(Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE);
        return pref.getBoolean(Constants.DELETED, false);
    }

    public static void setStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        editor.putBoolean(Constants.DELETED, status);
        editor.apply();
    }

    public static void setReportsList(Context context, List<Report> reports){
        SharedPreferences.Editor prefsEditor = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(reports);
        Log.d(TAG, "onDataChange: json: " + json);
        prefsEditor.putString(Constants.REPORT_LIST_KEY, json);
        prefsEditor.apply();
    }

    public static List<Report> getReportsList(Context context){
        SharedPreferences mPrefs = context.getSharedPreferences(Constants.PRIVATE_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(Constants.REPORT_LIST_KEY, Constants.DEFAULT_VALUE);
        Type type = new TypeToken<List<Report>>() {}.getType();
        return gson.fromJson(json, type);
    }


}
