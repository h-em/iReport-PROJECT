package com.android.ireport.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserReport {

    private String title;
    private String details;
    private Date currentDate;
    private int latitude;
    private int longitude;
    private int status;
    private User user;
}
