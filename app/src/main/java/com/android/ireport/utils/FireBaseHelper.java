package com.android.ireport.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.ireport.R;
import com.android.ireport.model.Photo;
import com.android.ireport.model.Report;
import com.android.ireport.model.User;
import com.android.ireport.model.UserData;
import com.android.ireport.model.UserExtras;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FireBaseHelper {
    private static final String TAG = "FireBaseHelper";

    //current user UID
    private String userId;
    private double mPhotoUploadProgress = 0;

    private Context mContext;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mReference;
    StorageReference mStorageReference;

    public FireBaseHelper(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
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


            //TODO
            //get somehow the report obj from db

          /*
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
         */

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

    public List<Report> getUserReports(DataSnapshot dataSnapshot) {

        List<Report> reports = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.child("user_reports")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {
            Log.d(TAG, "getUserReports(): report: " + ds);

            Report report = new Report();
            report.setReport(ds.getValue(Report.class));
            reports.add(report);
        }
        return reports;
    }

    public void updateUsername(String username) {

        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        if (username != null) {
            mReference.child("users").child(userId).child("username").setValue(username);
            Toast.makeText(mContext, "username updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "username is null", Toast.LENGTH_SHORT).show();
        }
    }


    public int getNumberOfUserReports(DataSnapshot dataSnapshot) {
        int count = 0;

        for (DataSnapshot ds : dataSnapshot
                .child("user_reports")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {

            count++;
        }
        return count;
    }


    public int getNumberOfResolvedUserReports(DataSnapshot dataSnapshot) {
        int count = 0;

        for (DataSnapshot ds : dataSnapshot
                .child("user_reports")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {

            if (ds.child("status").getValue().equals("done")) {
                count++;
            }
        }
        return count;
    }

    public void uploadNewReportAndPhoto(String photoType, String reportDescription, int imageCount, String imageUrl, Object o, String longitude, String latitude) {

        FilePaths filePaths = new FilePaths();
        if (photoType.equals("new_photo")) {
            Log.d(TAG, "uploadNewReportAndPhoto: Upload new photo.");

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (imageCount + 1));

            Bitmap bitmap = ImageManager.getBitmap(imageUrl);
            byte[] bytes = ImageManager.getByteFromBitmap(bitmap, 100);

            UploadTask uploadTask;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG, "uploadNewReportAndPhoto: addOnSuccessListener() -> taskSnapshot: " + taskSnapshot.toString());

                Uri firebaseImageUrl = taskSnapshot.getUploadSessionUri();
                Toast.makeText(mContext, "photo uploaded success!", Toast.LENGTH_SHORT).show();

                //inset report and photo in db
                addReportToDatabase(latitude, longitude, reportDescription, firebaseImageUrl.toString());

                //navigate to the main page

            }).addOnFailureListener(e -> {
                Log.d(TAG, "uploadNewReportAndPhoto: addOnFailureListener() -> Exception: " + e);
                Toast.makeText(mContext, "photo upload failed! ", Toast.LENGTH_SHORT).show();

            }).addOnProgressListener(taskSnapshot -> {
                Log.d(TAG, "uploadNewReportAndPhoto: addOnProgressListener() -> taskSnapshot: " + taskSnapshot.toString());
                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                if (progress - 15 > mPhotoUploadProgress) {
                    Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                    mPhotoUploadProgress = progress;
                }
                Log.d(TAG, "addOnProgressListener() -> photo upload progress: " + progress);
            });
        } else if (photoType.equals("user_profile_photo)")) {
            Log.d(TAG, "uploadNewReportAndPhoto: Upload new photo for user profile.");
            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

            StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "user_profile_photo");

        }
    }

    public Photo addPhotoToDatabase(String downloadUrl) {
        Log.d(TAG, "addPhotoToDatabase: adding photo to database");


        //creez id-ul( random string)  si il iau din db
        String newPhotoKey = mReference.child("photos").push().getKey();
        //creez obiectul
        Photo photo = new Photo();
        photo.setImage_url(downloadUrl);
        photo.setPhoto_id(newPhotoKey);

        // insert la poza in nodul "photos"
        mReference.child("photos").child(newPhotoKey).setValue(photo);

        return photo;
    }


    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/GMT+2"));
        return sdf.format(new Date());
    }


    public void addReportToDatabase(String latitude, String longitude, String details, String downloadUrl) {

        // set photo into "photos" node and generate a random id for it
        Photo photo = addPhotoToDatabase(downloadUrl);


        //creez id-ul(random string) si il iau din db
        String newReportKey = mReference.child("reports").push().getKey();
        //creez obiectul
        Report report = new Report();
        report.setCurrent_date(getTimeStamp());
        report.setDetails(details);
        report.setLatitude(latitude);
        report.setLongitude(longitude);
        report.setStatus("new");
        report.setPhoto(photo);

        //insert in nodul "reports" -- DONE
        mReference.child("reports").child(newReportKey).setValue(report);

        //insert in nodul de user_reports in functie de id_ul userului curent -- DONE
        mReference.child("user_reports").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(newReportKey).setValue(report);
    }


    // e nefolosita momentan
/*
    private String setStatus(String status) {
        Log.d(TAG, "setStatus: set status.");

        String current_status = "undefined";
        if (status.equalsIgnoreCase("new")) {
            current_status = "new";
        } else if (status.equalsIgnoreCase("in progress")) {
            current_status = "in progress";
        } else if (status.equalsIgnoreCase("done")) {
            current_status = "done";
        }
        return current_status;
    }*/
}
