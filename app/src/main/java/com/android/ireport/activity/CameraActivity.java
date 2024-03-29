package com.android.ireport.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.ireport.R;
import com.android.ireport.adapter.SectionsPagerAdapter;
import com.android.ireport.fragment.GalleryFragment;
import com.android.ireport.fragment.PhotoFragment;
import com.android.ireport.utils.Constants;
import com.android.ireport.utils.Permissions;
import com.google.android.material.tabs.TabLayout;


public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        mContext = CameraActivity.this;

        askForPermission();

        //set flag  -> it depends were the intent is came from
        setFlag();
    }

    private void setFlag() {
        if(getIntent().hasExtra(Constants.CAMERA_FLAG)){
            Log.d(TAG, "setFlag(): set Flag to 0.");
            getIntent().setFlags(0);
        }else{
            Log.d(TAG, "setFlag(): flag: " + getIntent().getFlags());
        }
    }

    private void askForPermission() {
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
                CameraActivity.this,
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

        int permissionRequest = ActivityCompat.checkSelfPermission(CameraActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions: \n Permissions was not granted for: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions: \n Permissions was granted for: " + permission);
            return true;
        }
    }
}
