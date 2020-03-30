package com.android.ireport.cameraFlow;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.ireport.R;
import com.android.ireport.utils.Permissions;
import com.google.android.material.tabs.TabLayout;



public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";

    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        mContext = ShareActivity.this;

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }

    }

    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }


    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = findViewById(R.id.viewPager_container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs_bottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");

    }

    public int getTask() {
        Log.d(TAG, "getTask: TASK: " + getIntent().getFlags());
        return getIntent().getFlags();
    }


    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    public boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }


    public boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permissions was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permissions was granted for: " + permission);
            return true;
        }
    }


    private void setupBottomNavigationView() {
      /*  Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);*/
    }
}
