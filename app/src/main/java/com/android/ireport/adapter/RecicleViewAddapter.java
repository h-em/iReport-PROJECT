package com.android.ireport.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.model.UserReport;

import java.util.List;

public class RecicleViewAddapter extends RecyclerView.Adapter<RecicleViewAddapter.ViewHolder> {
    private static final String TAG = "RecicleViewAddapter";

    private List<UserReport> mUserReports;
    private Context mContext;

    public RecicleViewAddapter(List<UserReport> mUserReports, Context mContext) {
        this.mUserReports = mUserReports;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_user_reports, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mUserReports.get(position).getDetails());

                Toast.makeText(mContext, mUserReports.get(position).getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mUserReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView image;
        TextView username;
        TextView reportdetails;
        TextView date;
        TextView location;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            reportdetails = itemView.findViewById(R.id.report_details);
            date = itemView.findViewById(R.id.date_of_report);
            location = itemView.findViewById(R.id.location_report);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
