package com.android.ireport.activities;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.ireport.fragments.HomeFragment;
import com.android.ireport.R;
import com.android.ireport.fragments.UserDetailsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView conditionTextView;
    Button buttonFoggy;
    Button buttonSunny;
    private BottomNavigationView bottomNavigationView;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionReference = databaseReference.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        buttonFoggy = findViewById(R.id.foggy_button);
        buttonSunny = findViewById(R.id.sunny_button);

        bottomNavigationView.setOnNavigationItemReselectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemReselectedListener navListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    Fragment fragment = null;

                    switch (item.getItemId()) {
                        case R.id.menu_item_user_requests:
                            fragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            break;
                        case R.id.menu_item_user_details:
                            fragment = new UserDetailsFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                            break;
                        case R.id.menu_item_camera:

                            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);

                            if (intent != null) {
                                startActivity(intent);
                            }

                            break;
                        default:
                            break;
                    }
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }
            };


    @Override
    protected void onStart() {
        super.onStart();

        conditionReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                conditionTextView = findViewById(R.id.textView);

                String text = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: " + text);
                conditionTextView.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());

            }
        });
    }

}
