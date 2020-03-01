package com.android.ireport.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile);
        Log.d(TAG, "onCreate: EditProfileActivity started.");

        mContext = EditProfileActivity.this;

        onPressBackArrow();
    }

    private void onPressBackArrow(){

        ImageView backArrow = findViewById(R.id.back_arrow_icon);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to UserProfileActivity");
                finish();
            }
        });
    }
}
