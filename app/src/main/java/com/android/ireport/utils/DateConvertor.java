package com.android.ireport.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConvertor {

    public static String dateToString(Date date){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
}
