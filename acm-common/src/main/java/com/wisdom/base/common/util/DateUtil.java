package com.wisdom.base.common.util;

import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获取今天
     * @return String
     * */
    public static String getToday(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    /**
     * 获取昨天
     * @return String
     * */
    public static String getYestoday(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time);
    }
    /**
     * 获取本月开始日期
     * @return String
     * **/
    public static String getMonthStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00 ";
    }
    /**
     * 获取本月最后一天
     * @return String
     * **/
    public static String getMonthEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }
    /**
     * 获取本周的第一天
     * @return String
     * **/
    public static String getWeekStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00";
    }
    /**
     * 获取本周的最后一天
     * @return String
     * **/
    public static String getWeekEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time=cal.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59";
    }
    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static String getYearStart(){
        return new SimpleDateFormat("yyyy").format(new Date())+"-01-01 00:00:00";
    }

    /**
     * 获取本年的最后一天
     * @return String
     * **/
    public static String getYearEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currYearLast)+" 23:59:59";
    }

    /**
     * 当前季度的开始时间
     * @return
     */
    public static String getQuarterStart() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        if (currentMonth >= 1 && currentMonth <= 3)
            calendar.set(Calendar.MONTH, 0);
        else if (currentMonth >= 4 && currentMonth <= 6)
            calendar.set(Calendar.MONTH, 3);
        else if (currentMonth >= 7 && currentMonth <= 9)
            calendar.set(Calendar.MONTH, 6);
        else if (currentMonth >= 10 && currentMonth <= 12)
            calendar.set(Calendar.MONTH, 9);
        calendar.set(Calendar.DATE, 1);
        Date currQuarterStart = calendar.getTime();
        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(currQuarterStart) + " 00:00:00";
        return startDate;
    }

    /**
     * 当前季度的结束时间
     *
     * @return
     */
    public static String getQuarterEnd() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            calendar.setTime(longSdf.parse(getQuarterStart()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, 2);
        Date currQuarterEnd = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currQuarterEnd) + " 23:59:59";
    }

    /**
     * 获取两个时间差（分钟）
     * @return
     */
    public static int getTimeChaByTwoTime(Date time1,Date time2){
        if(ObjectUtils.isEmpty(time1) || ObjectUtils.isEmpty(time2)){
            return 0;
        }
        long from3 = time1.getTime();
        long to3 = time2.getTime();
        int minutes = (int) ((to3 - from3) / (1000 * 60));
        return minutes;
    }

    /**
     * 获取两个时间差（天数）
     * @return
     */
    public static int getDaysChaByTwoTime(Date time1,Date time2){
        if(time1 == null || time2 == null){
            return 0;
        }
        long from3 = time1.getTime();
        long to3 = time2.getTime();
        int minutes = (int) ((to3 - from3) / (1000 * 60 * 60 * 24));
        return minutes;
    }

    /**
     * 格式化时间
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDatePoor(Date startDate, Date endDate) {
        if(ObjectUtils.isEmpty(startDate) || ObjectUtils.isEmpty(endDate)){
            return "";
        }
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;

        if(day != 0){
            return day + "天";
        }else if(hour!=0){
            return hour + "小时";
        }else{
            return min + "分钟";
        }
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String formatDate(Date date){
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String formatDateTime(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date stringToDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}