package com.android.ireport.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.fragment.EditReportFragment;
import com.android.ireport.model.Report;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private static final String TAG = "RecycleViewAdapter";

    private List<Report> mUserReports;
    private Context mContext;
    private FragmentManager mFragmentManager;


    public RecycleViewAdapter(List<Report> mUserReports, Context mContext, FragmentManager fragmentManager) {
        this.mUserReports = mUserReports;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_user_reports, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.title.setText(mUserReports.get(position).getTitle());
        //holder.username.setText(mUserReports.get(position).getUser().getUsername());
        String date = mUserReports.get(position).getCurrent_date();
        holder.date.setText(date);
        String location = mUserReports.get(position).getLatitude() + " " + mUserReports.get(position).getLongitude();
        holder.location.setText(location);
        //holder.reportDetails.setText(mUserReports.get(position).getDetails());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFragmentManager.beginTransaction().replace(R.id.fragment_container, new EditReportFragment()).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mUserReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView image;
        TextView title;
        //TextView username;
        //TextView reportDetails;
        TextView date;
        TextView location;
        //TextView status;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            //username = itemView.findViewById(R.id.item);
            //reportDetails = itemView.findViewById(R.id.it);
            date = itemView.findViewById(R.id.item_date);
            location = itemView.findViewById(R.id.item_location);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
