package com.exam.support.util.date;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by lijun7 on 2016/10/14.
 */
public class DateUtils {
    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60000L;
    public static final long MILLIS_PER_HOUR = 3600000L;
    public static final long MILLIS_PER_DAY = 86400000L;

    public DateUtils() {
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }

    public static Date add(Date date, int calendarFiled, int amount) {
        if(null == date) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarFiled, amount);
            return c.getTime();
        }
    }

    public static Date setYears(Date date, int amount) {
        return set(date, 1, amount);
    }

    public static Date setMonths(Date date, int amount) {
        return set(date, 2, amount);
    }

    public static Date setDays(Date date, int amount) {
        return set(date, 5, amount);
    }

    public static Date setHours(Date date, int amount) {
        return set(date, 11, amount);
    }

    public static Date setMinutes(Date date, int amount) {
        return set(date, 12, amount);
    }

    public static Date setSeconds(Date date, int amount) {
        return set(date, 13, amount);
    }

    public static Date setMilliSeconds(Date date, int amount) {
        return set(date, 14, amount);
    }

    public static Date set(Date date, int calendarFiled, int amount) {
        if(null == date) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.set(calendarFiled, amount);
            return c.getTime();
        }
    }

    public static Calendar toCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public static Date now() {
        return Calendar.getInstance().getTime();
    }

    public static String getDate() {
        return DateFormatUtils.formatDate(Calendar.getInstance().getTime());
    }

    public static String getDatetime() {
        return DateFormatUtils.formatDatetime(Calendar.getInstance().getTime());
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if(null != date1 && null != date2) {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            return isSameDay(cal1, cal2);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(null != cal1 && null != cal2) {
            return cal1.get(0) == cal2.get(0) && cal1.get(1) == cal2.get(1) && cal1.get(6) == cal2.get(6);
        } else {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    public static Date addTwo(Date d1, Date d2) {
        return null != d1 && null != d2?new Date(d1.getTime() + d2.getTime()):null;
    }

    public static int minusToYear(Date d1, Date d2) {
        if(null != d1 && null != d2) {
            Calendar cl1 = Calendar.getInstance();
            Calendar cl2 = Calendar.getInstance();
            cl1.setTime(d1);
            cl2.setTime(d2);
            return cl1.get(1) - cl2.get(1);
        } else {
            throw new IllegalArgumentException("d1和d2都不能为null");
        }
    }

    public static int minusToMonth(Date d1, Date d2) {
        if(null != d1 && null != d2) {
            Calendar cl1 = Calendar.getInstance();
            Calendar cl2 = Calendar.getInstance();
            cl1.setTime(d1);
            cl2.setTime(d2);
            return (cl1.get(1) - cl2.get(1)) * 12 + cl1.get(2) - cl2.get(2);
        } else {
            throw new IllegalArgumentException("d1和d2都不能为null");
        }
    }

    public static int minusToDay(Date d1, Date d2) {
        return (int)(minusToMilliSecond(d1, d2) / 86400000L);
    }

    public static int minusToHours(Date d1, Date d2) {
        return (int)(minusToMilliSecond(d1, d2) / 3600000L);
    }

    public static long minusToMinutes(Date d1, Date d2) {
        return minusToMilliSecond(d1, d2) / 60000L;
    }

    public static long minusToSeconds(Date d1, Date d2) {
        return minusToMilliSecond(d1, d2) / 1000L;
    }

    public static long minusToMilliSecond(Date d1, Date d2) {
        if(null != d1 && null != d2) {
            return d1.getTime() - d2.getTime();
        } else {
            throw new IllegalArgumentException("d1和d2都不能为null");
        }
    }
}