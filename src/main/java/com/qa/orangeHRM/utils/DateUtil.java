package com.qa.orangeHRM.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String getCurrentTime(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        String formattedTime = currentTime.format(formatter).replace(":","");
        return formattedTime;
    }

    public static String getCurrentDateYYYYMMDD(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentTime.format(formatter);
        return formattedDate;
    }

}
