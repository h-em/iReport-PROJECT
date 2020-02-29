package com.android.ireport.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.ireport.R;
import com.android.ireport.adapter.SectionPagerAdapter;
import com.android.ireport.fragment.EditReportFragment;
import com.android.ireport.utils.BottomNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Context mContext = MainActivity.this;

   /* TextView conditionTextView;
    Button buttonFoggy;
    Button buttonSunny;
    private BottomNavigationView bottomNavigationView;*

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionReference = databaseReference.child("condition");
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: starting the app.");

        setupBottomNavigationView();
        setupViewPager();
       /* bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        conditionTextView = findViewById(R.id.textView);

        // buttonFoggy = findViewById(R.id.foggy_button);
        //buttonSunny = findViewById(R.id.sunny_button);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new UserDetailsFragment()).commit();
        }

        bottomNavigationView.setOnNavigationItemReselectedListener(navListener);*/
    }

   /* private BottomNavigationView.OnNavigationItemReselectedListener navListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    Intent intent;
                    Fragment fragment;
                    switch (item.getItemId()) {
                        case R.id.menu_item_user_requests:
                            // trebuie sa ma intorc pe main


                            finish();
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            fragment = new UserReportFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            break;
                        case R.id.menu_item_user_details:

                            intent = new Intent(getApplicationContext(), UserReportActivity.class);
                            startActivity(intent);

                            fragment = new UserDetailsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

                            break;
                        case R.id.menu_item_camera:
                            intent = new Intent(getApplicationContext(), CameraActivity.class);
                            startActivity(intent);

                            break;
                        default:
                            break;
                    }
                }
            };*/

/*
    @Override
    protected void onStart() {
        super.onStart();

        conditionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String text = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: " + text);
                conditionTextView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }*/

    //BottomNavigationView setup
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView);
    }

    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager(),0);
        adapter.addFragment(new EditReportFragment());
        ViewPager2 viewPager = findViewById(R.id.container_view_pager_2);
        //viewPager.setAdapter(adapter);

        ////ca sa mearga viewPager2 tre sa am o clasa care implementez Adapter si apoi setez adapterul in viewPager2

    }
}
