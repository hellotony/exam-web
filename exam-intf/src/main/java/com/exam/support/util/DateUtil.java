package com.exam.support.util;

import com.exam.support.util.date.DateUtils;
import com.exam.support.util.string.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil extends DateUtils {
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_1 = "yyyy/MM/dd";
    public static final String FORMAT_YYYY_MM_DD_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_mm = "yyyy-MM-dd HH:mm";
    public static Date parseDate(String dateStr, String format) throws ParseException {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }
    public static Date parseDate(String dateStr){
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Date parseDateHm(String dateStr){
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_mm);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String getNowDateTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_mm_ss);
        return sdf.format(new Date());
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        String retStr = sdf.format(date);
        return retStr;
    }

    public static String formatDateHm(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YYYY_MM_DD_HH_mm);
        String retStr = sdf.format(date);
        return retStr;
    }
}
