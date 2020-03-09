package com.android.ireport.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.ireport.R;
import com.android.ireport.adapter.RecycleViewAdapter;
import com.android.ireport.adapter.SectionPagerAdapter;
import com.android.ireport.fragment.EditReportFragment;
import com.android.ireport.login.LoginActivity;
import com.android.ireport.model.User;
import com.android.ireport.model.UserReport;
import com.android.ireport.utils.BottomNavigationHelper;
import com.android.ireport.utils.UniversalImageLoader;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Context mContext = MainActivity.this;
    private List<UserReport> userReports = new ArrayList<>();

    // Firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting the app.");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthSetup();

        //create ImageLoader Object
        initImageLoader();
        setupBottomNavigationView();
        //display recyclerVie
        initRecyclerView();


        // nu face nimic momentan
        //setupViewPager();
    }

    private void setupBottomNavigationView() {
        //BottomNavigationView setup
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext,this, bottomNavigationView);
    }

    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycleview.");

        userReports.add(new UserReport("title","details", Calendar.getInstance().getTime().toString(),
                4564,654564, 1, new User("ion","ion@mail")));
        userReports.add(new UserReport("title","details2",Calendar.getInstance().getTime().toString(),
                456422,654564222, 1, new User("ion2","ion@mail2")));
        userReports.add(new UserReport("title","details", Calendar.getInstance().getTime().toString(),
                4564,654564, 1, new User("ion","ion@mail")));
        userReports.add(new UserReport("title","details2",Calendar.getInstance().getTime().toString(),
                456422,654564222, 1, new User("ion2","ion@mail2")));
        userReports.add(new UserReport("title","details", Calendar.getInstance().getTime().toString(),
                4564,654564, 1, new User("ion","ion@mail")));
        userReports.add(new UserReport("title","details2",Calendar.getInstance().getTime().toString(),
                   456422,654564222, 1, new User("ion2","ion@mail2")));


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecycleViewAdapter adapter = new RecycleViewAdapter(userReports, mContext, getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }

    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(new EditReportFragment());
        ViewPager2 viewPager = findViewById(R.id.container_view_pager_2);
        //viewPager.setAdapter(adapter);

        ////ca sa mearga viewPager2 tre sa am o clasa care implementez Adapter si apoi setez adapterul in viewPager2
    }


    private void firebaseAuthSetup(){
        Log.d(TAG, "firebaseAuthSetup: setting up firebase auth.");
        mAuthStateListener = firebaseAuth -> {

            FirebaseUser user = firebaseAuth.getCurrentUser();
            checkCurrentUser(user);

            if(user != null){
                Log.d(TAG, "onAuthStateChanged: user is signed in. userId: " + user.getUid());
                //user  is signed  in

            }else{
                Log.d(TAG, "onAuthStateChanged: user is signed out.");
                //user is signed out
            }
        };

    }


    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
        if(user == null){
            Toast.makeText(mContext, "User is not logged", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
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
    }
}
