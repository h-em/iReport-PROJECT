package com.android.ireport.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.ireport.R;
import com.android.ireport.model.User;
import com.android.ireport.model.UserData;
import com.android.ireport.model.UserExtras;
import com.android.ireport.model.Report;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public UserData getUserData(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserData: get data from db.");

        Report report = new Report();
        User user = new User();
        UserExtras userExtras = new UserExtras();


        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            //for Reports node
            if (ds.getKey().equals("reports")) {
                Log.d(TAG, "getUserData: dataSnapshot: " + ds);

                try {
                    report.setCurrent_date(ds.child(userId).getValue(Report.class).getCurrent_date());
                    report.setDetails(ds.child(userId).getValue(Report.class).getDetails());
                    report.setLatitude(ds.child(userId).getValue(Report.class).getLatitude());
                    report.setLongitude(ds.child(userId).getValue(Report.class).getLongitude());
                    report.setStatus(ds.child(userId).getValue(Report.class).getStatus());
                    report.setTitle(ds.child(userId).getValue(Report.class).getTitle());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getReportData: NullPointerException:  " + e.getMessage());
                }
            }

            //for users node
            if (ds.getKey().equals("users")) {
                Log.d(TAG, "getUser: dataSnapshot: " + ds);

                try {
                    user.setEmail(ds.child(userId).getValue(User.class).getEmail());
                    user.setUsername(ds.child(userId).getValue(User.class).getUsername());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserData: NullPointerException:  " + e.getMessage());
                }
            }

            //for users_account node
            if (ds.getKey().equals("users_account")) {
                Log.d(TAG, "getUserExtras: dataSnapshot: " + ds);

                try {
                    userExtras.setProfile_photo(ds.child(userId).getValue(UserExtras.class).getProfile_photo());
                    userExtras.setReports_nr(ds.child(userId).getValue(UserExtras.class).getReports_nr());
                    userExtras.setResolved_reports_nr(ds.child(userId).getValue(UserExtras.class).getResolved_reports_nr());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserData: NullPointerException:  " + e.getMessage());
                }
            }
        }
        return new UserData(user, userExtras, report);
    }


    public User getUser(DataSnapshot dataSnapshot, String userId) {

        User user = new User();
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                if (ds.getKey().equals("users")) {
                    Log.d(TAG, "getUser: user: " + dataSnapshot);

                    user = ds.child(userId).getValue(User.class);
                }
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "getReports: " + e.getMessage());
        }

        return user;
    }

    public UserExtras getUserExtras(DataSnapshot dataSnapshot, String userId) {

        UserExtras userExtras = new UserExtras();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {

            try {
                if (ds.getKey().equals("users_account")) {
                    Log.d(TAG, "getUserExtras: userExtras: " + dataSnapshot);

                    userExtras = ds.child(userId).getValue(UserExtras.class);
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "getReports: " + e.getMessage());
            }
        }

        return userExtras;
    }

    public List<Report> getReports(DataSnapshot dataSnapshot, String userId) {

        List<Report> reports = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot.child("reports").getChildren()) {

            try {
                if (ds.getKey().equals(userId)) {
                    Log.d(TAG, "getUser: user: " + dataSnapshot);

                    reports.add(ds.child(userId).getValue(Report.class));
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "getReports: " + e.getMessage());
            }
        }

        return reports;
    }

    public void updateUsername(String username) {

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        mReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> map = new HashMap();
        map.put("username",username);
        mReference.child("users").child(userId).updateChildren(map);

    }



}
