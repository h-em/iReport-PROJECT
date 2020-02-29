package com.android.ireport.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.android.ireport.R;
import com.android.ireport.activity.CameraActivity;
import com.android.ireport.activity.MainActivity;
import com.android.ireport.activity.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {
    private static final String TAG = "BottomNavigationHelper";

    public static void enableNavigation(final Context context, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_user_reports:
                        Intent userReportsIntent = new Intent(context, MainActivity.class);
                        context.startActivity(userReportsIntent);
                        break;
                    case R.id.menu_item_camera:
                        Intent cameraIntent = new Intent(context, CameraActivity.class);
                        context.startActivity(cameraIntent);
                        break;
                    case R.id.menu_item_user_profile:
                        Intent userProfileIntent = new Intent(context, UserProfileActivity.class);
                        context.startActivity(userProfileIntent);
                        break;
                }
            }
        });

    }
}
