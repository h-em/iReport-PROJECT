package com.android.ireport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    private String current_date;
    private String details;
    private int latitude;
    private int longitude;
    private String status;
    private Photo photo;
}
