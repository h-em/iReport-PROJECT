package com.android.ireport.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.activity.MainActivity;
import com.android.ireport.utils.FireBaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private Context mContext;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mUsername;

    private TextView mLoadingPleaseWait;
    private Button mRegisterButton;
    private ProgressBar mProgressBar;

    private String email, username, password;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FireBaseHelper mFireBaseHelper;

    //private FirebaseMethods firebaseMethods;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started.");

        mContext = RegisterActivity.this;

        mFireBaseHelper = new FireBaseHelper(mContext);

        mEmail = findViewById(R.id.input_email_register);
        mPassword = findViewById(R.id.input_password_register);
        mUsername = findViewById(R.id.input_username_register);

        mLoadingPleaseWait = findViewById(R.id.loadingPleaseWait_register);
        mRegisterButton = findViewById(R.id.register_button);
        mProgressBar = findViewById(R.id.progressBar_register);

        mProgressBar.setVisibility(View.GONE);
        mLoadingPleaseWait.setVisibility(View.GONE);

        setupFirebaseAuth();
        onClickRegisterButton();
    }

    //Setup the fireBase auth object
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();


        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                // User is signed in
                Log.d(TAG, "setupFirebaseAuth: signed_in: " + user.getUid());

                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange");

                        //check if a username already exists
                        if (mFireBaseHelper.checkIfUsernameAlreadyExists(username, dataSnapshot)) {
                            Random random = new Random();
                            for (int i = 0; i < 3; i++) {
                                username += random.nextInt(10);
                            }
                            Log.d(TAG, "onDataChange: username already exists so we randomNumber a random at the end of your username");
                        }

                        //create and add user to the database
                        mFireBaseHelper.addNewUser(username, email, "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                goToHomeIfUserIsLogged();

            } else {
                // User is signed out
                Log.d(TAG, "setupFirebaseAuth: signed_out");
            }
        };
    }


    private void onClickRegisterButton() {
        mRegisterButton.setOnClickListener(v -> {

            email = mEmail.getText().toString();
            username = mUsername.getText().toString();
            password = mPassword.getText().toString();

            if (email.equals("") || username.equals("") || password.equals("")) {
                Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mLoadingPleaseWait.setVisibility(View.VISIBLE);

                mFireBaseHelper.registerUser(email, password, username);
                mProgressBar.setVisibility(View.GONE);
                mLoadingPleaseWait.setVisibility(View.GONE);
            }
        });
    }

    private void goToHomeIfUserIsLogged() {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
            this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } else {
            Log.d(TAG, "goToHomeIfUserIsLogged: ");
        }
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
