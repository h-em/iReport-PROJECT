package com.android.ireport.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.adapter.RecicleViewAddapter;
import com.android.ireport.model.User;
import com.android.ireport.model.UserReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UserReportFragment extends Fragment {
    private static final String TAG = "UserReportFragment";

    private List<UserReport> userReports = new ArrayList<>();
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_user_reports);
        Log.d(TAG, "onCreate: started.");

        initRecyclerView();
    }
*/
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView: init recycleview.");

        userReports.add(new UserReport("details",Calendar.getInstance().getTime(),
                4564,654564, new User("ion","ion@mail")));
        userReports.add(new UserReport("details2",Calendar.getInstance().getTime(),
                456422,654564222, new User("ion2","ion@mail2")));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerv_view);
        RecicleViewAddapter adapter = new RecicleViewAddapter(userReports, recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_user_reports, container, false);

        initRecyclerView(view);
        return view;
    }

}