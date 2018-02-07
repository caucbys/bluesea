/*
 * Copyright (C) Bluesea Fintech, Inc, 北京晨灏科技有限公司，Bluesea Fintech USA, LLC - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited Proprietary and confidential
 */

package com.alphalion.crawl.application.util;


import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by liwt on 7/24/16.
 */
public class TimeUtils {

    public static final long INFINITE_DATE_MS = 90000000000000L;
    public static final String INFINITE_DATE = "4821-12-27";
    public static final String END_OF_DAY_SEC = "23:59:59";

    public static final DateTimeZone TIMEZONE_US = DateTimeZone.forID("US/Eastern");
    public static final DateTimeZone TIMEZONE_HK = DateTimeZone.forID("Asia/Hong_Kong");

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmm");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyyMMdd");
    private static final DateTimeFormatter CCYYMMDD_DATE_FORMAT = DateTimeFormat.forPattern("CCYYMMDD");
    private static final DateTimeFormatter MMDDCCYY_DATE_FORMAT = DateTimeFormat.forPattern("MM/DD/CCYY");

    private static final DateTimeFormatter DATE_FORMAT_1 = DateTimeFormat.forPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_FORMAT_2 = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter TIME_FORMAT_US = TIME_FORMAT.withZone(TIMEZONE_US);
    private static final DateTimeFormatter DATE_FORMAT_US = DATE_FORMAT.withZone(TIMEZONE_US);

    private static final DateTimeFormatter TIME_FORMAT_HK = TIME_FORMAT.withZone(TIMEZONE_HK);
    private static final DateTimeFormatter DATE_FORMAT_HK = DATE_FORMAT.withZone(TIMEZONE_HK);

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");

    private static final DateTimeFormatter TIME_FORMAT_ZONE = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss z");

    private static final DateTimeFormatter TIME_FORMAT_ZONE_HK = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss z"
    ).withZone(TIMEZONE_HK);

    private static final DateTimeFormatter CLOSE_TIME_HK = DateTimeFormat.forPattern("MM-dd HH:mm:ss z"
    ).withZone(TIMEZONE_HK);

    private static final DateTimeFormatter CLOSE_TIME = DateTimeFormat.forPattern("MM-dd HH:mm:ss z");

    private static final DateTimeFormatter TIME_FORMAT_ZONE_US = DateTimeFormat.forPattern("yyyyMMdd HH:mm:ss z"
    ).withZone(TIMEZONE_US);

    private static final DateTimeFormatter CLOSE_TIME_US = DateTimeFormat.forPattern("MM-dd HH:mm:ss z"
    ).withZone(TIMEZONE_US);

    private static final DateTimeFormatter HOUR_TRADING_TIME_US = DateTimeFormat.forPattern("yyyyMMdd HH:mm z"
    ).withZone(TIMEZONE_US);


    public static long getTime(String timeStr) {
        return TIME_FORMAT.parseMillis(timeStr);
    }


    public static String printTime(long timestamp) {
        return TIME_FORMAT.print(timestamp);
    }


    public static long getDate(String timeStr) {
        return DATE_FORMAT.parseMillis(timeStr);
    }


    public static String printDate(long timestamp) {
        return DATE_FORMAT.print(timestamp);
    }

    public static long getTimeET(String timeStr) {
        return TIME_FORMAT_US.parseMillis(timeStr);
    }

    public static String printTimeET(long timestamp) {
        return TIME_FORMAT_US.print(timestamp);
    }

    public static long getDateET(String timeStr) {
        return DATE_FORMAT_US.parseMillis(timeStr);
    }

    public static String printDateET(long timestamp) {
        return DATE_FORMAT_US.print(timestamp);
    }

    public static long getTimestamp(String timeStr) {
        return TIMESTAMP_FORMAT.parseMillis(timeStr);
    }

    public static String printTimestamp(long timestamp) {
        return TIMESTAMP_FORMAT.print(timestamp);
    }

    public static long getTimeZoneET(String timeStr) {
        return TIME_FORMAT_ZONE_US.parseMillis(timeStr);
    }

    public static String printTimeZoneET(long timestamp) {
        return TIME_FORMAT_ZONE_US.print(timestamp);
    }

    /**
     * 把日期精确到天
     *
     * @param cal
     * @return
     */
    public static Date getDay(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDay(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 把date往前或者往后推几天,diff为正数,则往后推,反之往前
     *
     * @param date
     * @param diff
     * @return
     */
    public static Date getDiffDate(Date date, int diff) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.DATE, diff);
        return c.getTime();
    }

    /**
     * 取两个日期之间相隔天数
     *
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public static int getDiscrepantDays(Date dateStart, Date dateEnd) {
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24);
    }

    public static Date parseCCYYMMDDDate(String dateStr) {
        return CCYYMMDD_DATE_FORMAT.parseDateTime(dateStr).toDate();
    }

    public static Date parseMMDDCCYYDate(String dateStr) {
        return MMDDCCYY_DATE_FORMAT.parseDateTime(dateStr).toDate();
    }

    public static Date parseDate(String dateStr) {
        return DATE_FORMAT_1.parseDateTime(dateStr).toDate();
    }

    public static Date parseDateForSecond(String secondStr) {
        return DATE_FORMAT_2.parseDateTime(secondStr).toDate();
    }

    /**
     * 按照日期格式转化为字符串 yyyy-MM-dd
     *
     * @param d
     * @return
     */
    public static String printDate(Date d) {
        return DATE_FORMAT_1.print(d.getTime());
    }

    public static String printDateForSecond(Date d) {
        return DATE_FORMAT_2.print(d.getTime());
    }

    /**
     * 把date往前或者往后推几秒,diff为正数,则往后推,反之往前
     *
     * @param date
     * @param diff
     * @return
     */
    public static Date getDiffSecond(Date date, int diff) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);
        c.add(Calendar.SECOND, diff);
        return c.getTime();
    }

    public static final java.sql.Date get(String str, String formate) {
        if (str == null || str.length() == 0) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat(formate);
        try {
            Date date = dateFormat.parse(str);
            if (date != null) {
                return new java.sql.Date(date.getTime());
            }
        } catch (ParseException e) {
            return null;
        }
        return null;
    }


    /**
     * 获得当前时间
     * 统一接口，以后进行时区转换
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 获得当前时间
     * 统一接口，以后进行时区转换
     *
     * @return
     */
    public static Timestamp getTimestamp() {
        return new Timestamp(getNow().getTime());
    }


    public static int compareDate(Date d1, Date d2) {
        if (null == d1 && null == d2) {
            return 0;
        }
        if (null == d1) {
            return -1;
        }

        if (null == d2) {
            return 1;
        }

        Long res = d1.getTime() - d2.getTime();
        return res.intValue();
    }

}
