package com.android.ireport.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ireport.R;
import com.android.ireport.fragment.EditReportFragment;
import com.android.ireport.model.Report;
import com.android.ireport.utils.FireBaseHelper;
import com.android.ireport.utils.UniversalImageLoader;

import java.util.List;


public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private static final String TAG = "RecycleViewAdapter";

    private List<Report> mUserReports;
    private Context mContext;
    private FragmentManager mFragmentManager;
    private FireBaseHelper mFirebaseHelper;

    public RecycleViewAdapter(List<Report> mUserReports, Context mContext, FragmentManager fragmentManager) {
        this.mUserReports = mUserReports;
        this.mContext = mContext;
        this.mFragmentManager = fragmentManager;
        this.mFirebaseHelper = new FireBaseHelper(mContext);
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

        holder.date.setText(/*"Date: " + */mUserReports.get(position).getCurrent_date());
        holder.latitude.setText("Latitude: " + mUserReports.get(position).getLatitude());
        holder.longitude.setText("Longitude: " + mUserReports.get(position).getLongitude());
        //holder.reportDetails.setText("Details: " + mUserReports.get(position).getDetails());
        holder.status.setText("Status: " + mUserReports.get(position).getStatus());
        UniversalImageLoader.setImage(mUserReports.get(position).getPhoto().getImage_url(), holder.image, null, "");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle arguments = new Bundle();
                arguments.putString("report_date", mUserReports.get(position).getCurrent_date());
                arguments.putString("report_details", mUserReports.get(position).getDetails());
                arguments.putString("report_latitude", mUserReports.get(position).getLatitude());
                arguments.putString("report_longitude", mUserReports.get(position).getLongitude());
                arguments.putString("report_photo_url", mUserReports.get(position).getPhoto().getImage_url());
                arguments.putString("report_status", mUserReports.get(position).getStatus());
                arguments.putString("report_id", mUserReports.get(position).getReport_id());

                EditReportFragment editReportFragment = new EditReportFragment();
                editReportFragment.setArguments(arguments);

                mFragmentManager.beginTransaction().replace(R.id.middle_layout, editReportFragment).addToBackStack(null).commit();
            }
        });
    }

    public void deleteItem(String reportId){
        mFirebaseHelper.deleteItemForAGivenReportId(reportId);
    }

    @Override
    public int getItemCount() {
        return this.mUserReports.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        //TextView reportDetails;
        TextView date;
        TextView latitude;
        TextView longitude;
        TextView status;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //reportDetails = itemView.findViewById(R.id.report_details_next_activity);
            date = itemView.findViewById(R.id.item_date);
            latitude = itemView.findViewById(R.id.item_latitude_report);
            //latitude.setVisibility(View.GONE);
            longitude = itemView.findViewById(R.id.item_longitude_report);
            //longitude.setVisibility(View.GONE);
            image = itemView.findViewById(R.id.image_report_item_report);
            status = itemView.findViewById(R.id.status_report);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
