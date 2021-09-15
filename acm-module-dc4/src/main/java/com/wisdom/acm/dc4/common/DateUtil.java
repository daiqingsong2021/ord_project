/**
 * Project Name:tyfw
 * File Name:DateUtil.java
 * Package Name:com.jsumt.util
 * Date:2017年6月13日上午11:26:59
 * Copyright (c) 2017, wuyf5@asiainfo-linkage.com All Rights Reserved.
 */

package com.wisdom.acm.dc4.common;

import org.apache.commons.lang.StringUtils;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtil
{

    // 默认日期格式
    public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";
    
    public static final String DATE_CHINA_FORMAT = "yyyy年MM月dd日";
    public static final String DATETIME_CHINA_FORMAT = "yyyy年MM月dd日 HH:mm:ss";

    // 默认时间格式
    public static final String DATETIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";

    // 日期格式化
    private static DateFormat dateFormat = null;

    // 时间格式化
    private static DateFormat dateTimeFormat = null;

    private static DateFormat timeFormat = null;

    private static Calendar gregorianCalendar = null;

    static
    {
        dateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);
        dateTimeFormat = new SimpleDateFormat(DATETIME_DEFAULT_FORMAT);
        timeFormat = new SimpleDateFormat(TIME_DEFAULT_FORMAT);
        gregorianCalendar = new GregorianCalendar();
    }

    /**
     * 日期格式化yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public static Date formatDate(String date, String format)
    {
        try
        {
            return new SimpleDateFormat(format).parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 日期格式化yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public static String getDateFormat(Date date)
    { 
         return dateFormat.format(date);
    }

    /**
     * 日期格式化yyyy-MM-dd HH:mm:ss
     * 
     * @param date
     * @return
     */
    public static String getDateTimeFormat(Date date)
    {
        return dateTimeFormat.format(date);
    }

    /**
     * 时间格式化
     * 
     * @param date
     * @return HH:mm:ss
     */
    public static String getTimeFormat(Date date)
    {
        return timeFormat.format(date);
    }

    /**
     * 根据指定标准 获取日期格式化
     *
     * @param date
     * @return
     */
    public static String getDateFormat(Date date, String formatStr)
    {
        if (StringUtils.isNotEmpty(formatStr)&& date!=null)
        {
            return new SimpleDateFormat(formatStr).format(date);
        }
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @return
     */
    public static Date getDateFormat(String date)
    {
        try
        {
            return dateFormat.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间格式化
     * 默认yyyy-MM-dd
     * @param date
     * @return
     */
    private static Date getDateTimeFormat(String date)
    {
        try
        {
            return dateTimeFormat.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当前日期(yyyy-MM-dd)
     * 
     * @return
     */
    public static Date getNowDate()
    {
        return DateUtil.getDateFormat(dateFormat.format(new Date()));
    }

    /**
     * 获取当前日期  星期一日期
     * 
     * @return date
     */
    public static Date getFirstDayOfWeek()
    {
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前日期 星期日日期
     * 
     * @return date
     */
    public static Date getLastDayOfWeek()
    {
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定日期 星期一日期
     * 
     * @return date
     */
    public static Date getFirstDayOfWeek(Date date)
    {
        if (date == null)
        {
            return null;
        }
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek()); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定日期 星期日日期
     * 
     * @return date
     */
    public static Date getLastDayOfWeek(Date date)
    {
        if (date == null)
        {
            return null;
        }
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek() + 6); // Monday
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前月的第一天
     * 
     * @return date
     */
    public static Date getFirstDayOfMonth()
    {
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取当前月的最后一天
     * 
     * @return
     */
    public static Date getLastDayOfMonth()
    {
        gregorianCalendar.setTime(new Date());
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date)
    {
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        return gregorianCalendar.getTime();
    }

    /**
     * 获取指定月的最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date)
    {
        gregorianCalendar.setTime(date);
        gregorianCalendar.set(Calendar.DAY_OF_MONTH, 1);
        gregorianCalendar.add(Calendar.MONTH, 1);
        gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);
        return gregorianCalendar.getTime();
    }


    /**
     * 根据天数，获取日期的前多少天的 日期
     * @param date
     * @param days
     * @return
     */
    public static Date getDayBefore(Date date,int days)
    {
        gregorianCalendar.setTime(date);
        int day = gregorianCalendar.get(Calendar.DATE);
        gregorianCalendar.set(Calendar.DATE, day - days);
        return gregorianCalendar.getTime();
    }


    /**
     * 根据日期获取多少天后的日期
     * @param date
     * @param days
     * @return
     */
    public static Date getDayAfter(Date date,int days)
    {
        gregorianCalendar.setTime(date);
        int day = gregorianCalendar.get(Calendar.DATE);
        gregorianCalendar.set(Calendar.DATE, day + days);
        return gregorianCalendar.getTime();
    }

    /**
     * 根据日期获取多少月后的日期
     * @param date
     * @param months
     * @return
     */
    public static Date getMonthAfter(Date date,int months)
    {
        gregorianCalendar.setTime(date);
        int month = gregorianCalendar.get(Calendar.MONTH);
        gregorianCalendar.set(Calendar.MONTH, month + months);
        return gregorianCalendar.getTime();
    }
    /**
     * 获取当前年
     * 
     * @return
     */
    public static int getNowYear()
    {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * 
     * @return
     */
    public static int getNowMonth()
    {
        Calendar d = Calendar.getInstance();
        return d.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当月天数
     * 
     * @return
     */
    public static int getNowMonthDay()
    {
        Calendar d = Calendar.getInstance();
        return d.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取时间段的每一天
     * 
     * @return 日期列表
     */
    public static List<Date> getEveryDay(Date startDate, Date endDate)
    {
        if (startDate == null || endDate == null)
        {
            return null;
        }
        // 格式化日期(yy-MM-dd)
        startDate = DateUtil.getDateFormat(DateUtil.getDateFormat(startDate));
        endDate = DateUtil.getDateFormat(DateUtil.getDateFormat(endDate));
        List<Date> dates = new ArrayList<Date>();
        gregorianCalendar.setTime(startDate);
        dates.add(gregorianCalendar.getTime());
        while (gregorianCalendar.getTime().compareTo(endDate) < 0)
        {
            // 加1天
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);
            dates.add(gregorianCalendar.getTime());
        }
        return dates;
    }


    /**
     * 判断时间格式是否正确
     * @param str
     * @return
     */
    public static boolean isValidDate(String str,String pattern) {
        DateFormat formatter = new SimpleDateFormat(pattern);
        try{
            Date date = (Date)formatter.parse(str);
            return str.equals(formatter.format(date));
        }catch(Exception e){
            return false;
        }
    }

    /**
     * 转换时间为 XMLGregorianCalendar 格式
     *
     * @param date
     * @return
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_DEFAULT_FORMAT);

        GregorianCalendar cal = new GregorianCalendar();
        try {
            cal.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return gc;
    }

    /**
     * XMLGregorianCalendar 转Date
     * @param cal
     * @return
     */
    public static  Date convertToDate(XMLGregorianCalendar cal){
        GregorianCalendar ca = null;
        if(cal!=null && cal.toGregorianCalendar()!=null){
            ca = cal.toGregorianCalendar();
            return ca.getTime();
        }
        return null;
    }

    public static void main(String args[])
    {
       System.out.println(DateUtil.getDateFormat(DateUtil.getFirstDayOfWeek()));
        boolean a = isSameWeek("2019-9-10", "2019-9-12");
        if (a) {
            System.out.println("是同一周！");
        } else {
            System.out.println("不是同一周！");
        }
    }

    /**
     * 判断两个时间是否在同一周
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeek(String date1, String date2)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try
        {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setFirstDayOfWeek(Calendar.MONDAY);//将周一设为一周第一天
        cal2.setFirstDayOfWeek(Calendar.MONDAY);
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (subYear == 0 && (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)))// 同一年的同一周
        {
                return true;
        }
        return false;
    }

    //将java.util.Date 转换为java8 的java.time.LocalDateTime,默认时区为东8区
    public static LocalDateTime dateConvertToLocalDateTime(Date date) {
        return date.toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
    }


    //将java8 的 java.time.LocalDateTime 转换为 java.util.Date，默认时区为东8区
    public static Date localDateTimeConvertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.toInstant(ZoneOffset.of("+8")));
    }

        /**
          * 判断时间是否在时间段内
          * @param nowTime
          * @param beginTime
          * @param endTime
          * @return
          */
    public static boolean betweenCalendar(Date nowTime, Date beginTime, Date endTime) {
            Calendar date = Calendar.getInstance();
            date.setTime(nowTime);
            //设置开始时间
            Calendar begin = Calendar.getInstance();
            begin.setTime(beginTime);
            //设置结束时间
            Calendar end = Calendar.getInstance();
            end.setTime(endTime);
            //处于开始时间之后，和结束时间之前的判断
            if (date.after(begin) && date.before(end)) {
                return true;
            } else {
                return false;
            }
        }


    public static Date parseUsDateTime02(String timeStr){
        String pattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        try {
            Date date = new SimpleDateFormat(pattern, Locale.US).parse(timeStr);
            return date;
        } catch (ParseException e) {
            System.out.println("时间解析错误");
        }
        return null;
    }
}
