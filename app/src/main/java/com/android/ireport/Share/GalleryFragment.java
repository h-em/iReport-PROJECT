package com.android.ireport.Share;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ireport.R;
import com.android.ireport.adapter.GridImgAdapter;
import com.android.ireport.utils.FilePaths;
import com.android.ireport.utils.FileSearch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 5/28/2017.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    private static final int NUM_GRID_COLUMNS = 3;


    private GridView mGridView;
    private ImageView mGalleryImage;
    private ProgressBar mProgressBar;
    private Spinner mDirectorySpinner;

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
        mProgressBar.setVisibility(View.GONE);
        directories = new ArrayList<>();


        //close activity on x press
        ImageView shareClose = view.findViewById(R.id.close_view_gallery_bar);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment.");
                getActivity().finish();
            }
        });

        TextView nextScreen = view.findViewById(R.id.next_activity_textView);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");


                //if(isRootTask()){
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra("selected_image", mSelectedImage);
                    startActivity(intent);
                    /*
                }else{
/*
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra("selected_image", mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }*/

            }
        });

        init();

        return view;
    }


    private boolean isRootTask(){
        if(((ShareActivity)getActivity()).getTask() == 0){
            return true;
        }
        else{
            return false;
        }
    }

    private void init(){
        FilePaths filePaths = new FilePaths();

        //check for other folders inside "/storage/emulated/0/pictures"
        if (FileSearch.getDirectoryPath(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPath(filePaths.PICTURES);
        }
        directories.add(filePaths.CAMERA);


        List<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            Log.d(TAG, "init: directory: " + directories.get(i));
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);
        }

        //pun in spinner lista cu numele directoarelor
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, directoryNames);
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


    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directory chosen: " + selectedDirectory);
        final List<String> imgURLs = FileSearch.getFilePath(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        mGridView.setColumnWidth(imageWidth);

        //use the grid adapter to adapter the images to gridview
        GridImgAdapter adapter = new GridImgAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        mGridView.setAdapter(adapter);

        //set the first image to be displayed when the activity fragment view is inflated
        try{
            setImage(imgURLs.get(0), mGalleryImage, mAppend);
            mSelectedImage = imgURLs.get(0);
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException: " +e.getMessage() );
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected an image: " + imgURLs.get(position));

                setImage(imgURLs.get(position), mGalleryImage, mAppend);
                mSelectedImage = imgURLs.get(position);
            }
        });

    }


    private void setImage(String imgURL, ImageView image, String append){
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































