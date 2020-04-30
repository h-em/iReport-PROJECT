package com.android.ireport.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.ireport.R;
import com.android.ireport.utils.UniversalImageLoader;

public class EditReportFragment extends Fragment {
    private static final String TAG = "EditReportFragment";

    ImageView backArrow;
    ImageView editReport;
    ImageView itemImage;

    TextView date;
    TextView location;
    TextView status;
    TextView details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backArrow = view.findViewById(R.id.back_arrow_icon_report_toolbar);
        editReport = view.findViewById(R.id.edit_icon_reports_list);
        itemImage = view.findViewById(R.id.image_edit_report);

        date = view.findViewById(R.id.item_date);
        details = view.findViewById(R.id.item_details);
        location = view.findViewById(R.id.item_location);
        status = view.findViewById(R.id.item_status);

        setViewsOnFragment();

        onBackArrowPress();
    }

    private void setViewsOnFragment() {

        Bundle args = getArguments();

        date.setText("Date: " + args.getString("report_date"));
        details.setText("Description: " + args.getString("report_details"));
        location.setText("Latitude: " + args.getString("report_latitude")
                + "  Longitude: " + args.getString("report_longitude"));
        status.setText("Current status: " + args.getString("report_status"));

        String imgUrl = args.getString("report_photo_url");
        Log.d(TAG, "setImage: got new image url: " + imgUrl);
        UniversalImageLoader.setImage(imgUrl, itemImage, null, "");

    }


    public void onBackArrowPress() {
        backArrow.setOnClickListener((v) -> {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove(this);
            trans.commit();
            manager.popBackStack();
        });
    }

    public void onEditPress() {
        editReport.setOnClickListener((v) -> {
            //open new dialog or fragment to edit details section
        });
    }

}
