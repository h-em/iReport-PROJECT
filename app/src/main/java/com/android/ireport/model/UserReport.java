package com.android.ireport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReport {

    private String title;
    private String details;
    private String currentDate;
    private int latitude;
    private int longitude;
    private int status;
    private User user;
}
