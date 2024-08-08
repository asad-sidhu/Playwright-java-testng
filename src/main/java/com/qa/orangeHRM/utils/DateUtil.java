package com.qa.orangeHRM.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static String convertTo12HourFormat(String time24) {
        LocalTime time = LocalTime.parse(time24, DateTimeFormatter.ofPattern("HH:mm"));

        String time12 = time.format(DateTimeFormatter.ofPattern("hh:mm a"));

        return time12;
    }

}
