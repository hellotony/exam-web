package com.exam.support.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by lijun7 on 2016/10/14.
 */
public class DateFormatUtils {

    private static final ThreadLocal<HashMap<String, SimpleDateFormat>> DATE_LOCAL = new ThreadLocal();
    private static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public DateFormatUtils() {
    }

    private static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat sdf = null;
        HashMap local = (HashMap)DATE_LOCAL.get();
        if(null == local) {
            HashMap map = new HashMap();
            sdf = new SimpleDateFormat(format);
            map.put(format, sdf);
            DATE_LOCAL.set(map);
        } else {
            sdf = (SimpleDateFormat)local.get(format);
        }

        if(null == sdf) {
            sdf = new SimpleDateFormat(format);
            local.put(format, sdf);
        }

        return sdf;
    }

    public static String format(String pattern, Date date) {
        return getDateFormat(pattern).format(date);
    }

    public static Date parse(String pattern, String dateStr) throws ParseException {
        return getDateFormat(pattern).parse(dateStr);
    }

    public static String formatDate(Date date) {
        return getDateFormat("yyyy-MM-dd").format(date);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        return getDateFormat("yyyy-MM-dd").parse(dateStr);
    }

    public static String formatDatetime(Date date) {
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date parseDatetime(String dateStr) throws ParseException {
        return getDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
    }

}
