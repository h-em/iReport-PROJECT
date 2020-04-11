package com.android.ireport.cameraFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ireport.R;
import com.android.ireport.activity.EditProfileActivity;
import com.android.ireport.activity.MainActivity;
import com.android.ireport.adapter.GridImgAdapter;
import com.android.ireport.utils.FilePaths;
import com.android.ireport.utils.FileSearch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private static final int NUM_GRID_COLUMNS = 3;

    private GridView mGridView;
    private ImageView mGalleryImage;
    private ProgressBar mProgressBar;
    private Spinner mDirectorySpinner;
    private TextView mNextScreen;

    private List<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        Log.d(TAG, "onCreateView: started.");

        mGalleryImage = view.findViewById(R.id.galeryImgeView);
        mGridView = view.findViewById(R.id.gidView_fragment_gallery);
        mDirectorySpinner = view.findViewById(R.id.spinner_gallery_bar);
        mProgressBar = view.findViewById(R.id.progressbarr_gallery_fragment);

        //hide progressBar
        mProgressBar.setVisibility(View.GONE);

        //close activity on X press
        ImageView closeFragment = view.findViewById(R.id.close_view_gallery_bar);
        onClickToCloseTheFragment(closeFragment);

        //go to the next screen
        mNextScreen = view.findViewById(R.id.next_activity_textView);
        goToNextScreen(mNextScreen);

        init();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        directories = new ArrayList<>();
    }

    private void goToNextScreen(TextView nextScreen) {
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");

                if (isRootTask()) {
                    //send an intent to start NextActivity + extra image
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    startActivity(intent);

                } else {
                    //send intent to the EditProfileActivity + selected image
                    Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_activity), getString(R.string.edit_profile_activity));
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                }
            }
        });

    }

    private void onClickToCloseTheFragment(ImageView closeFragment) {
        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private boolean isRootTask() {
        if (((CameraActivity) getActivity()).getTask() == 0) {
            return true;
        }
        return false;
    }

    private void init() {
        FilePaths filePaths = new FilePaths();

        //check for other folders inside "/storage/emulated/0/pictures"
        if (FileSearch.getDirectoryPath(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPath(filePaths.PICTURES);
        }
        directories.add(filePaths.CAMERA);

        //get the directory names
        List<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            Log.d(TAG, "init: directory: " + directories.get(i));
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);
        }

        //set the dropDown the list that contains the name of directories
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDirectorySpinner.setAdapter(adapter);

        //dropdown with all array values
        mDirectorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected: " + directories.get(position));

                //setup our image grid for the directory chosen
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final List<String> imgURLs = FileSearch.getFilePath(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        mGridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapter the images to gridview
        GridImgAdapter adapter = new GridImgAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        mGridView.setAdapter(adapter);

        if (imgURLs.size() > 0) {
            //set the first image to be displayed when the activity fragment view is inflated
            try {
                setImage(imgURLs.get(0), mGalleryImage, mAppend);
                mSelectedImage = imgURLs.get(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " + e.getMessage());
            }

        }
        //set the clicked image to be displayed
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));

                setImage(imgURLs.get(position), mGalleryImage, mAppend);
                mSelectedImage = imgURLs.get(position);
            }
        });

    }


    private void setImage(String imgURL, ImageView image, String append) {
        Log.d(TAG, "setImage: setting image");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

}































