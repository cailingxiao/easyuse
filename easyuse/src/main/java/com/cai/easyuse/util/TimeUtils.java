package com.cai.easyuse.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cailingxiao
 * @date 2016年4月10日
 */
public class TimeUtils {
    private static final SimpleDateFormat SDF_LINE = new SimpleDateFormat(
            "yyyy-MM-dd");
    private static final SimpleDateFormat SDF_CH = new SimpleDateFormat(
            "yyyy年MM月dd日");
    private static final long DAY = 3600L * 1000L * 24L;

    private TimeUtils() {

    }

    /**
     * 将这种格式的：yyyy年MM月dd日转换成long型
     *
     * @param chStr
     *
     * @return
     */
    public static long parseCh(String chStr) {
        Date date;
        try {
            date = SDF_CH.parse(chStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    /**
     * 将格式为2016-05-06转换成long型
     *
     * @param lineStr
     *
     * @return
     */
    public static long parseLine(String lineStr) {
        Date date = null;
        try {
            date = SDF_LINE.parse(lineStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    /**
     * 将long型转换成如 2016-04-10
     *
     * @param times
     *
     * @return
     */
    public static String formatLine(long times) {
        return format(SDF_LINE, times);
    }

    /**
     * 将long型转化成如2016年4月10日
     *
     * @param times
     *
     * @return
     */
    public static String formatCh(long times) {
        return format(SDF_CH, times);
    }

    public static long next(int day) {
        long offset = System.currentTimeMillis() + DAY * day;
        return offset;
    }

    public static long next() {
        return next(1);
    }

    private static String format(DateFormat df, long times) {
        return df.format(new Date(times));
    }

    /**
     * 获取时间描述
     *
     * @param currentTime
     * @param givenTime
     *
     * @return
     */
    public static String getTimeBetween(long currentTime, long givenTime) {
        long time = currentTime - givenTime;
        String str = "";
        if (time / (1000 * 3600 * 24) > 0) {
            str = (time / (1000 * 3600 * 24)) + "天前";
        } else if (time / (1000 * 3600) > 0) {
            str = (time / (1000 * 3600)) + "小时前";
        } else if (time / (1000 * 60) > 0) {
            str = (time / (1000 * 60)) + "分钟前";
        } else {
            str = (time / (1000)) + "秒前";
        }
        return str;
    }
}
