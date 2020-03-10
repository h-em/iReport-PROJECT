package com.android.ireport.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserExtras {
    private String profile_photo;
    private int reports_nr;
    private int resolved_reports_nr;


    public UserExtras(String profile_photo, int reports_nr, int resolved_reports_nr) {
        this.profile_photo = profile_photo;
        this.reports_nr = reports_nr;
        this.resolved_reports_nr = resolved_reports_nr;
    }
}
