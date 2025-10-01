package com.example.mypost._core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class MyDateUtil {

    public static String timestampFormat(Timestamp time) {

        Date currentDate = new Date(time.getTime());

        return DateFormatUtils.format(currentDate, "yyyy-MM-dd HH:mm");
    }
}
