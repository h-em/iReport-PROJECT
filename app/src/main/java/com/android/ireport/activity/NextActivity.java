package com.android.ireport.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.ireport.R;
import com.android.ireport.service.LocationService;
import com.android.ireport.utils.Constants;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.UniversalImageLoader;
import com.android.ireport.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FireBaseHelper mFirebaseHelper;

    private Context mContext;
    private EditText mDetails;


    private String mAppend = "file:/";
    private int imageCount = 0;
    private Intent intent;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        mContext = NextActivity.this;

        //start service to obtain current location
        startService(new Intent(mContext, LocationService.class));

        // Initialize FireBase stuff
        mAuth = FirebaseAuth.getInstance();
        mFirebaseHelper = new FireBaseHelper(NextActivity.this);

        mDetails = findViewById(R.id.report_details_next_activity);

        setupFirebaseAuth();

        onClickBackArrow();

        onClickSendButton();

        setImage();
    }

    private void onClickSendButton() {
        TextView send = findViewById(R.id.send_textView);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
                //upload the image to firebase
                Toast.makeText(NextActivity.this, "Attempting to upload new photo", Toast.LENGTH_SHORT).show();
                String reportDescription = mDetails.getText().toString();

                // check if user has permission to access location
                if(ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(NextActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.REQUEST_CODE_LOCATION_PERMISSION);

                    ActivityCompat.requestPermissions(NextActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constants.REQUEST_CODE_LOCATION_PERMISSION);
                }else{
                    //get current location
                    //getCurrentLocation();
                    startService(new Intent(mContext, LocationService.class));
                }
                String latitude = Utils.getLatitude(mContext);
                String longitude = Utils.getLongitude(mContext);

                // if the image comes from GalleryFragment with extra imageUrl
                if (intent.hasExtra(getString(R.string.selected_image))) {
                    String imgUrl = intent.getStringExtra(getString(R.string.selected_image));
                    mFirebaseHelper.uploadNewReportAndPhoto(reportDescription, imageCount, imgUrl, latitude, longitude);
                }
                // if the image comes from PhotoFragment with extra bitmap
                else if(intent.hasExtra(getString(R.string.selected_bitmap))){
                    Bitmap bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
                    mFirebaseHelper.uploadNewReportAndPhoto(reportDescription, imageCount, bitmap,latitude, longitude);
                }

                //small delay(it gives time for images to update into firebase)
                new Handler().postDelayed(() -> {
                    Intent gotToHome = new Intent(mContext, MainActivity.class);
                    startActivity(gotToHome);
                    finish();
                }, 2500);
            }
        });
    }

    private void onClickBackArrow() {
        ImageView backArrow = findViewById(R.id.back_arrow_icon_from_next_activity);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //getCurrentLocation();
                startService(new Intent(mContext, LocationService.class));
            }else{
                Toast.makeText(mContext, "Permisison denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setImage() {
        intent = getIntent();
        ImageView image = findViewById(R.id.image_taken);

        if (intent.hasExtra("selected_image")) {
            String imgUrl = intent.getStringExtra("selected_image");
            Log.d(TAG, "setImage: got new image url: " + imgUrl);
            UniversalImageLoader.setImage(imgUrl, image, null, mAppend);
        }
        else if(intent.hasExtra(getString(R.string.selected_bitmap))){
            Bitmap bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG, "setImage: got new bitmap");
            image.setImageBitmap(bitmap);
        }
    }


    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        Log.d(TAG, "onDataChange: image count: " + imageCount);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out.");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                imageCount = mFirebaseHelper.getNumberOfUserReports(dataSnapshot);
                Log.d(TAG, "onDataChange: image count: " + imageCount);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
