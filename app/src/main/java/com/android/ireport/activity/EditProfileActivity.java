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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfileActivity";

    private Context mContext;
    private ImageView mProfilePhoto;
    private EditText mUserName;
    private RelativeLayout mSaveLayout;
    private Button mChangePhoto;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mReference;
    private FireBaseHelper mFirebaseHelper;
    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Log.d(TAG, "onCreate: EditProfileActivity started.");

        mContext = EditProfileActivity.this;

        mProfilePhoto = findViewById(R.id.circleImageView_edit_user_profile);

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

    public void setUserProfileImage(){
        Log.d(TAG, "setUserProfileImage: setting profile image.");

        String imageURL = "https://tinyjpg.com/images/social/website.jpg";

        UniversalImageLoader.setImage(imageURL, mProfilePhoto, null, "");
    }

    public void saveEditedDetails(){

        String usename = mUserName.getText().toString();

        mSaveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for(DataSnapshot ds : dataSnapshot.child("users").getChildren()){
                            if(ds.getKey().equals(userId)) {
                                mFirebaseHelper.updateUsername(usename);
                            }
                        }

                        Toast.makeText(mContext, "username saved", Toast.LENGTH_SHORT).show();


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    public void setUsernameEditText(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mUserName.setText(extras.getString("username"));
        }
    }

    public void fromWork1(String username){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference
                .child("users")
                .orderByChild("username")
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mFirebaseHelper.updateUsername(username);
                Toast.makeText(mContext,"user udated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
