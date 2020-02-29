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
public class UserDetailsFragment extends Fragment {
    private static final String TAG = "UserDetailsFragment";

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_details, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}