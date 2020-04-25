package com.android.ireport.fragment;

import android.os.Bundle;
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

public class EditReportFragment extends Fragment {
    private static final String TAG = "EditReportFragment";

    ImageView backArrow;
    ImageView deleteIcon;
    ImageView editReport;
    ImageView itemImage;

    TextView title;
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
        deleteIcon = view.findViewById(R.id.delete_icon_bottom_appBar);
        editReport = view.findViewById(R.id.edit_icon_reports_list);
        itemImage = view.findViewById(R.id.image_edit_report);

        title = view.findViewById(R.id.item_title);
        date = view.findViewById(R.id.item_date);
        details = view.findViewById(R.id.item_details);
        location = view.findViewById(R.id.item_location);
        status = view.findViewById(R.id.item_status);

        onBackArrowPress();
    }


    public void onBackArrowPress(){
        backArrow.setOnClickListener((v)-> {
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
