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

    private String details;
    private Date currentDate;
    private int latitude;
    private int longitude;
    private User user;
}
