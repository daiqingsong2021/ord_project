package com.wisdom.acm.activiti.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {

    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String secToTime(long time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        time = time / 1000;
        if (time <= 0)
            return "00:00";
        else {
            minute = (int) time / 60;
            if (minute < 60) {
                second =  (int) time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour =  minute / 60;
                minute = minute % 60;
                second =  (int) time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }



    public static Date stringToDate(String dateString) {
        return stringToDate(dateString,null);
    }

    public static Date stringToDate(String dateString,String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(StringUtils.isEmpty(dateFormat)?DATE_FORMAT:dateFormat);
        ParsePosition pos = new ParsePosition(0);
        Date date = formatter.parse(dateString, pos);
        return date;
    }


    public static String dateToString(Date date) {
        return dateToString(date,null);
    }

    public static String dateToString(Date date,String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(StringUtils.isEmpty(dateFormat)?DATE_FORMAT:dateFormat);
        String dateString = formatter.format(date);
        return dateString;
    }

}
