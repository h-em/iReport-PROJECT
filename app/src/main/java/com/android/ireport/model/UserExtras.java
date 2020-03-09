package com.android.ireport.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserExtras {
    private String profile_photo;
    private int reports_nr;
    private int resolved_reports_nr;
}
