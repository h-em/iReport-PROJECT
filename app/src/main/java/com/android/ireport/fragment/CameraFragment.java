package com.android.ireport.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.ireport.R;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_camera, container, false);
        return view;
    }
}
