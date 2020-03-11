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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.ireport.R;
import com.android.ireport.activity.MainActivity;
import com.android.ireport.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final Boolean CHECK_IF_VERIFIED = false;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mPleaseWait;
    private TextView registerLink;
    private Button mLoginButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started.");

        mContext = LoginActivity.this;

        mEmail = findViewById(R.id.input_email_login);
        mPassword = findViewById(R.id.input_password_login);
        mProgressBar = findViewById(R.id.progressBar_login);
        mPleaseWait = findViewById(R.id.pleaseWait_login);
        mLoginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.link_signUp);

        mPleaseWait.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);


        setupFirebaseAuth();

        onClickLoginButton();
        onClickRegistrationLink();

    }


    //Setup the fireBase auth object
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                // User is signed in
                Log.d(TAG, "onAuthStateChanged: signed_in:" + user.getUid());
                // if the user is logged in, than navigate to MainActivity.class
                goToHomeIfUserIsLogged();
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged: signed_out");
            }
        };
    }


    private void onClickLoginButton() {

        mLoginButton.setOnClickListener(v -> {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();


            if (Utils.isStringNull(email) && Utils.isStringNull(password)) {
                Toast.makeText(mContext, "Email and Password should not be empty!", Toast.LENGTH_SHORT).show();
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
                mPleaseWait.setVisibility(View.VISIBLE);

                singInExistingUser(email, password);
            }
        });



    }


    public void singInExistingUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "singInExistingUser: success");
                        Toast.makeText(mContext, "Authentication success.", Toast.LENGTH_SHORT).show();

                    } else {

                        // If sign in fails, display a message to the user.
                        Log.d(TAG, "singInExistingUser: failure", task.getException());
                        Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }

                    mProgressBar.setVisibility(View.GONE);
                    mPleaseWait.setVisibility(View.GONE);
                });
    }


    private void goToHomeIfUserIsLogged() {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Log.d(TAG, "goToHomeIfUserIsLogged: ");
        }
    }

    public void onClickRegistrationLink() {
        registerLink.setOnClickListener(v -> {
            Log.d(TAG, "onClick: navigating to register screen");
            Intent intent = new Intent(mContext, RegisterActivity.class);
            startActivity(intent);
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
























