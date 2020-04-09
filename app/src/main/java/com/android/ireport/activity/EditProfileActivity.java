package com.android.ireport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.cameraFlow.CameraActivity;
import com.android.ireport.utils.Constatnts;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private Context mContext;
    private ImageView mProfilePhoto;
    private EditText mUserName;
    private RelativeLayout mSaveLayout;
    private Button mChangePhoto;
    private String finalUsername;

    //firebase
    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mReference;
    private FireBaseHelper mFirebaseHelper;
    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Log.d(TAG, "onCreate: EditProfileActivity started.");

        mContext = EditProfileActivity.this;

        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseHelper = new FireBaseHelper(mContext);


        mProfilePhoto = findViewById(R.id.circleImageView_edit_user_profile);
        mUserName = findViewById(R.id.edit_username_editView);
        mSaveLayout = findViewById(R.id.save_btn);
        mChangePhoto = findViewById(R.id.change_photo_button);

        userId = mAuth.getCurrentUser().getUid();

        setUsernameEditText();
        onPressBackArrow();
        saveEditedDetails();
        //set the image
        setUserProfileImage();

        getIncomingIntent();

        changeProfilePhoto();

    }

    private void changeProfilePhoto() {
        mChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: change user profile photo.");
                Intent intent = new Intent(mContext, CameraActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void getIncomingIntent() {
        Intent intent = getIntent();

        if (intent.hasExtra("selected_image")) {
            if (intent.hasExtra("return_to_activity")) {
                //set the new profile pictures
                String imageUrl = intent.getStringExtra("selected_image");
                mFirebaseHelper.uploadPhoto(imageUrl);
                finish();
            }
        }
    }


    private void onPressBackArrow() {
        ImageView backArrow = findViewById(R.id.back_arrow_icon);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                finish();
            }
        });
    }

    public void setUserProfileImage() {
        Log.d(TAG, "setUserProfileImage: setting profile image.");

        String imageURL = "https://tinyjpg.com/images/social/website.jpg";

        UniversalImageLoader.setImage(imageURL, mProfilePhoto, null, "");
    }

    public void saveEditedDetails() {
        mSaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUserName.getText().toString();
                Log.d(TAG, "saveEditedDetails: -------------->username: " + username);

                mFirebaseHelper.updateUsername(username);
            }
        });
    }

    public void setUsernameEditText() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mUserName.setText(extras.getString("username"));
        }
    }

}
