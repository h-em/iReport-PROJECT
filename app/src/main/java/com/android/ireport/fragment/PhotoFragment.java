package com.android.ireport.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ireport.R;
import com.android.ireport.activity.EditProfileActivity;
import com.android.ireport.activity.NextActivity;
import com.android.ireport.activity.CameraActivity;
import com.android.ireport.utils.Permissions;

import java.util.Objects;


public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constant
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 10;

    private Button mLauncherCameraButton;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");

        //camera button press
        mLauncherCameraButton = view.findViewById(R.id.button_launch_camera);
        onClickLauncherCameraButton();

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE ) {
            Log.d(TAG, "onActivityResult: photo was made.");
            Log.d(TAG, "onActivityResult: try to navigate to the next screen.");

            Bitmap bitmap;
            if (data.getExtras().get("data") != null) {
                bitmap = (Bitmap) data.getExtras().get("data");

                if (isRootTask()) {
                    try {
                        // intent comes from galleryFragment -> send the intent to the NextActivity + extras bitmap
                        Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                        Intent intent = new Intent(getActivity(), NextActivity.class);
                        intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                        startActivity(intent);
                    } catch (NullPointerException e) {
                        Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                    }
                } else {
                    try {
                        // intent comes from EditProfileActivity
                        //                  -> send the intent to the EditProfileActivity + extras bitmap
                        Log.d(TAG, "onActivityResult: received new bitmap from camera: " + bitmap);
                        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                        intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                        intent.putExtra(getString(R.string.return_to_activity), getString(R.string.edit_profile_activity));
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).finish();
                    } catch (NullPointerException e) {
                        Log.d(TAG, "onActivityResult: NullPointerException: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void onClickLauncherCameraButton() {
        mLauncherCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera.");

                if(getActivity() == null){
                    return;
                }

                if (((CameraActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((CameraActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean isRootTask() {
        if (((CameraActivity) getActivity()).getTask() == 0) return true;
        return false;
    }
}

































