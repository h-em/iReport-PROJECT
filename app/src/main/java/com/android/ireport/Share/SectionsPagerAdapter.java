package com.android.ireport.Share;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;


class SectionsPagerAdapter extends PagerAdapter {


    private FragmentManager mFragmentManager;
    public SectionsPagerAdapter(FragmentManager fragment){
        mFragmentManager = fragment;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    public void addFragment(Fragment galleryFragment) {
    }

}
