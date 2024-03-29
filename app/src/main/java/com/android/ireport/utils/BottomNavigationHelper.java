package com.android.ireport.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.android.ireport.R;
import com.android.ireport.activity.CameraActivity;
import com.android.ireport.activity.MainActivity;
import com.android.ireport.activity.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHelper {
    private static final String TAG = "BottomNavigationHelper";

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationView bottomNavigationView) {

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_user_reports:
                        Intent userReportsIntent = new Intent(context, MainActivity.class);
                        context.startActivity(userReportsIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.menu_item_camera:
                        Intent cameraIntent = new Intent(context, CameraActivity.class);
                        cameraIntent.putExtra(Constants.CAMERA_FLAG, 0);
                        context.startActivity(cameraIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.menu_item_user_profile:
                        Intent userProfileIntent = new Intent(context, ProfileActivity.class);
                        context.startActivity(userProfileIntent);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                }
                return false;
            }
        });

    }
}
