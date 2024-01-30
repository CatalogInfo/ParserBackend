package com.example.backend_parser.logs;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String formattedTime = formatter.format(date);
        return formattedTime;
    }

}
