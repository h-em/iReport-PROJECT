package com.android.ireport.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.ireport.R;
import com.android.ireport.model.User;
import com.android.ireport.model.UserExtras;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireBaseHelper {
    private static final String TAG = "FireBaseHelper";

    //current user UID
    private String userId;

    private Context mContext;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mReference;

    public FireBaseHelper(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
    }


    public void registerUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "registerUser: success.");
                        Toast.makeText(mContext, "Authentication success.", Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "registerUser: failure.", task.getException());
                        Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    public boolean checkIfUsernameAlreadyExists(String username, DataSnapshot dataSnapshot) {
        User user = new User();
        userId = mAuth.getCurrentUser().getUid();

        for (DataSnapshot ds : dataSnapshot.child(userId).getChildren()) {
            Log.d(TAG, "checkIfUsernameAlreadyExists -> dataSnapShot: " + ds);
            user.setUsername(ds.getValue(User.class).getUsername());
            if (user.getUsername().equals(username)) {
                Log.d(TAG, "checkIfUsernameAlreadyExists: username already exists in db.");
                return true;
            }
        }
        return false;
    }


    public void addNewUser(String username, String email, String profilePhoto) {

        ///add user

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        User user = new User(email, username);
        mReference.child(mContext.getString(R.string.users_node_name))
                .child(userId)
                .setValue(user);
        Log.d(TAG, "addNewUser: new user added.");

        //add user extras
        UserExtras userExtras = new UserExtras(
                profilePhoto,
                0,
                0);
        mReference.child(mContext.getString(R.string.users_account_node_name))
                .child(userId)
                .setValue(userExtras);

        Log.d(TAG, "addNewUser: new UserExtras added.");
    }
}
