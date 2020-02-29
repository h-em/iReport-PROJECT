package com.android.ireport.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.adapter.RecicleViewAddapter;
import com.android.ireport.model.User;
import com.android.ireport.model.UserReport;
import com.android.ireport.utils.BottomNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.NoArgsConstructor;


@NoArgsConstructor
public class UserReportActivity extends Activity {

    private static final String TAG = "UserReportActivity";

    private Context mContext = UserReportActivity.this;
    //private List<UserReport> userReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: started.");

        setupBottomNavigationView();
        //initRecyclerView();
    }

   /* private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recycleview.");

        userReports.add(new UserReport("details",Calendar.getInstance().getTime(),
                4564,654564, new User("ion","ion@mail")));
        userReports.add(new UserReport("details2",Calendar.getInstance().getTime(),
                456422,654564222, new User("ion2","ion@mail2")));

        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecicleViewAddapter adapter = new RecicleViewAddapter(userReports, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }*/
/*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        userReports.add(new UserReport("details",Calendar.getInstance().getTime(),
                4564,654564, new User("ion","ion@mail")));
        userReports.add(new UserReport("details2",Calendar.getInstance().getTime(),
                456422,654564222, new User("ion2","ion@mail2")));


        View view = inflater.inflate(R.layout.fragment_home_user_reports, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerv_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        RecicleViewAddapter adapter = new RecicleViewAddapter(userReports, this.getActivity());
        //recyclerView.setAdapter(adapter);

        return view;
    }
*/

    //BottomNavigationView setup
    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting bottomNavigationView.");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        BottomNavigationHelper.enableNavigation(mContext, bottomNavigationView);


    }

}