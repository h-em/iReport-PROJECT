package com.android.ireport.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.adapter.RecycleViewAdapter;
import com.android.ireport.login.LoginActivity;
import com.android.ireport.model.Report;
import com.android.ireport.utils.BottomNavigationHelper;
import com.android.ireport.utils.Constatnts;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.Permissions;
import com.android.ireport.utils.UniversalImageLoader;
import com.android.ireport.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int VERIFY_PERMIMSSION_REQUEST = 1;

    private Context mContext;
    private List<Report> reports = new ArrayList<>();


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mReference;
    private FireBaseHelper mFireBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting the app.");
        mContext = MainActivity.this;

        // Initialize FireBase stuff
        mAuth = FirebaseAuth.getInstance();
        mFireBaseHelper = new FireBaseHelper(mContext);
        firebaseAuthSetup();

        //create ImageLoader Object
        initImageLoader();
        setupBottomNavigationView();
        //display recyclerVie
        initRecyclerView();


        if (checkPermissionArray(Permissions.PERMISSIONS)) {

        } else {
            verifyPermission(Permissions.PERMISSIONS);
        }

    }


    private boolean checkPermissionArray(String[] permission) {

        for (int i = 0; i < permission.length; i++) {
            String check = permission[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }


    private boolean checkPermissions(String permission) {
        Log.d(TAG, "checkPermissions: checking permission");

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermissions:  permission was NOT granted for " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermissions:  permission was granted for " + permission);

            return true;
        }
    }

    public void verifyPermission(String[] permissions) {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                VERIFY_PERMIMSSION_REQUEST
        );
    }

    private void setupBottomNavigationView() {
        //BottomNavigationView setup
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext, this, bottomNavigationView);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView(): init recycleview.");

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference();


        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                List<Report> reports;
                reports = mFireBaseHelper.getUserReports(dataSnapshot);
                Log.d(TAG, "onDataChange(): mUserReports: " + reports);

                //save list into shared preferences
                Utils.setReportsList(mContext, Collections.EMPTY_LIST);
                Utils.setReportsList(mContext, reports);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //get list from shared preferences
        List<Report> reports = Utils.getReportsList(mContext);
        Log.d(TAG, "initRecyclerView(): reports: " + reports);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecycleViewAdapter adapter = new RecycleViewAdapter(reports, mContext, getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);

    }


    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
        if (user == null) {
            Toast.makeText(mContext, "User is not logged", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    private void firebaseAuthSetup() {
        Log.d(TAG, "firebaseAuthSetup: setting up firebase auth.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out.");
                }
            }
        };
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
