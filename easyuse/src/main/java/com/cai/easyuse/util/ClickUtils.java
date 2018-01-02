package com.cai.easyuse.util;

/**
 * 检查用户是否重复点击
 * 
 * @author Administrator
 * @date 2016年4月9日
 */
public final class ClickUtils {

    private static final long DEFAULT_TIME = 500;

    private ClickUtils() {

    }

    private static long sTimes = 0;

    /**
     * 检查点击是否有效(防止手抖点击多次) 表示这次点击距离上次点击的时间间隔是否小于{@link #DEFAULT_TIME}
     * 
     * 默认500毫秒
     * 
     * @return
     */
    public static boolean clickInner() {
        return clickInner(DEFAULT_TIME);
    }

    /**
     * 检查点击是否有效，这次距离上次点击的时间间隔是否小于millis毫秒
     * 
     * @param millis
     * @return
     */
    public static boolean clickInner(long millis) {
        boolean ret = true;
        long now = System.currentTimeMillis();
        if (now - sTimes < millis) {
            ret = true;
        } else {
            sTimes = now;
            ret = false;
        }
        return ret;
    }
}
