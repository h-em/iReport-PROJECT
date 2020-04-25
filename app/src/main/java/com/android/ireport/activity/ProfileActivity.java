package com.android.ireport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.login.LoginActivity;
import com.android.ireport.model.User;
import com.android.ireport.model.UserData;
import com.android.ireport.model.UserExtras;
import com.android.ireport.service.LocationService;
import com.android.ireport.utils.BottomNavigationHelper;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.UniversalImageLoader;
import com.android.ireport.utils.Utils;
import com.google.android.gms.location.LocationRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;


public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private Context mContext;

    private ProgressBar mProgressBar;
    private ImageView mProfilePhoto;

    private TextView mUsername;
    private TextView mReportsNumber;
    private TextView mResolvedReportsNumber;
    // private TextView mEmail;


    // Firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mReference;
    private FireBaseHelper mFirebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started.");

        mContext = ProfileActivity.this;
        mFirebaseHelper = new FireBaseHelper(mContext);

        mAuth = FirebaseAuth.getInstance();

        mUsername = findViewById(R.id.username_textview);
        mReportsNumber = findViewById(R.id.number_of_reports);
        mResolvedReportsNumber = findViewById(R.id.number_of_resolved_reports);
        mProgressBar = findViewById(R.id.profile_progressbar);
        mProgressBar.setVisibility(View.GONE);
        mProfilePhoto = findViewById(R.id.circleImageView);

        //fireBase setup
        firebaseAuthSetup();
        //get image from url and set the profile photo
        setUserProfileImage();
        setupBottomNavigationView();
        onEditProfileButtonPress();

        onLogOutButtonPress();
    }

    //BottomNavigationView setup
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext, this, bottomNavigationView);
    }

    private void onEditProfileButtonPress() {
        Button editProfileButton = findViewById(R.id.edit_profile_button);

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLocationService();

                Intent intent = new Intent(mContext, EditProfileActivity.class);
                intent.putExtra("username", mUsername.getText().toString());
                startActivity(intent);

            }
        });
    }

    private void startLocationService() {
        ///start the server to check if is working
        Intent intentService  = new Intent(mContext, LocationService.class);
        startService(intentService);
    }

    public void onLogOutButtonPress() {
        Button logOutButton = findViewById(R.id.logout_button);

        logOutButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                mAuth.signOut();
                Utils.setReportsList(mContext, Collections.EMPTY_LIST);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            } else {
                Toast.makeText(mContext, "User is still logged.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void setUserProfileImage() {
        Log.d(TAG, "setUserProfileImage: setting profile image.");


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                UserExtras userExtras = mFirebaseHelper.getUserExtras(dataSnapshot, userUid);

                String imageURL = userExtras.getProfile_photo();

                UniversalImageLoader.setImage(imageURL, mProfilePhoto, mProgressBar, "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


    }

    private void firebaseAuthSetup() {
        Log.d(TAG, "firebaseAuthSetup: setting up firebase auth.");

        mAuthStateListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged: user is signed in. userId: " + user.getUid());
                //user  is signed  in

            } else {
                Log.d(TAG, "onAuthStateChanged: user is signed out.");
                //user is signed out
            }
        };

        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get user  info from db
                UserData userData = mFirebaseHelper.getUserData(dataSnapshot);

                //get image for user
                //set user profile
                setProfileDetails(userData);

                //get reports numbers from db
                String reportsNumbers = Integer.toString(mFirebaseHelper.getNumberOfUserReports(dataSnapshot));
                mReportsNumber.setText(reportsNumbers);
                Log.d(TAG, "onDataChange: mReportsNumber: " + mReportsNumber.toString());

                //get resolved reports numbers from db
                String resolvedReportsNumbers = Integer.toString(mFirebaseHelper.getNumberOfResolvedUserReports(dataSnapshot));
                mResolvedReportsNumber.setText(resolvedReportsNumbers);
                Log.d(TAG, "onDataChange: mResolvedReportsNumber: " + mResolvedReportsNumber.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void setProfileDetails(UserData userData) {
        Log.d(TAG, "setProfileDetails: " + userData.getUserExtras());
        Log.d(TAG, "setProfileDetails: " + userData.getUser());

        User user = userData.getUser();
        UserExtras userExtras = userData.getUserExtras();

        //UniversalImageLoader.setImage(userExtras.getProfile_photo(), mProfilePhoto, null, "");
        mUsername.setText(user.getUsername());
        mReportsNumber.setText(userExtras.getReports_nr() + "");
        mResolvedReportsNumber.setText(userExtras.getResolved_reports_nr() + "");

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
        stopService(new Intent(this, LocationService.class));
    }
}
