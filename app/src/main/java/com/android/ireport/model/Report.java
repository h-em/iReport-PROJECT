package com.android.ireport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {

    private String current_date;
    private String details;
    private String latitude;
    private String longitude;
    private String status;
    private Photo photo;
    private String user_id;



    public void setReport(Report report) {
        this.current_date = report.getCurrent_date();
        this.details = report.getDetails();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.status = report.getStatus();
        this.photo = report.getPhoto();
        this.user_id = report.user_id;
    }
}
