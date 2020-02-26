package com.android.ireport.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.adapter.RecicleViewAddapter;
import com.android.ireport.model.User;
import com.android.ireport.model.UserReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Activity {

    private static final String TAG = "HomeFragment";

    private List<UserReport> userReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycleview.");

        userReports.add(new UserReport("details",Calendar.getInstance().getTime(),
                4564,654564, new User("ion","ion@mail")));
        userReports.add(new UserReport("details2",Calendar.getInstance().getTime(),
                456422,654564222, new User("ion2","ion@mail2")));

        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecicleViewAddapter adapter = new RecicleViewAddapter(userReports, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


}