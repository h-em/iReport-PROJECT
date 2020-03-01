package com.android.ireport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.utils.BottomNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";

    private Context mContext = UserProfileActivity.this;

    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        mProgressBar = findViewById(R.id.profile_progressbar);
        mProgressBar.setVisibility(View.GONE);

        setupBottomNavigationView();
        onEditProfileButtonPress();
        //initRecyclerView();
    }

    //BottomNavigationView setup
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView);
    }

    private void onEditProfileButtonPress(){
        Button editProfileButton = findViewById(R.id.edit_profile_button);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
