package com.cai.easyuse.util;

import android.util.Log;

import com.cai.easyuse.BuildConfig;

/**
 * 日志打印类
 * <p>
 * 如果需要打印，请先设置{@link #setDebugable()}
 * <p>
 * 上线请关闭
 *
 * @author cailingxiao
 * @date 2016/2/18
 */
public final class LogUtils {
    private static final boolean OUT_LINK = false;
    private static final String DEFAULT_TAG = "LogUtils";
    /**
     * 此常量用于控制是否打日志到Logcat中 release版本中本变量应置为false.
     */
    private static boolean sIsDebug = BuildConfig.DEBUG;

    private LogUtils() {

    }

    /**
     * 打开debug模式
     */
    public static void setDebugable() {
        sIsDebug = true;
        if (!BuildConfig.DEBUG) {
            i(DEFAULT_TAG, "检查到正式版本也打开了log，注意！！！");
        }
    }

    /**
     * 返回是否处于调试模式
     *
     * @return
     */
    public static boolean isDebug() {
        return sIsDebug;
    }

    /**
     * 系统名称 模块名称 接口名称 详细描述
     *
     * @param moduleName
     * @param actionName
     * @param detailMsg
     */
    public static void log(String moduleName, String actionName, String detailMsg) {
        if (sIsDebug) {
            printLog(moduleName + ":" + actionName, detailMsg + "", Log.DEBUG);
        }
    }

    /**
     * 打印debug级别的log.
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void d(String tag, Object str) {
        if (sIsDebug) {
            printLog(tag, str + "", Log.DEBUG);
        }
    }

    /**
     * 不打印debug log
     *
     * @param tag
     * @param str
     */
    public static void nd(String tag, Object str) {

    }

    /**
     * 打印debug级别的log.
     *
     * @param str 内容
     */
    public static void d(Object str) {
        if (sIsDebug) {
            printLog(DEFAULT_TAG, str + "", Log.DEBUG);
        }
    }

    /**
     * 不打印
     *
     * @param str
     */
    public static void nd(Object str) {

    }

    /**
     * 打印warning级别的log.
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void w(String tag, Object str) {
        if (sIsDebug) {
            printLog(tag, str + "", Log.WARN);
        }
    }

    /**
     * 不打印
     *
     * @param tag
     * @param str
     */
    public static void nw(String tag, Object str) {

    }

    /**
     * 打印warning级别的log.
     *
     * @param str 内容
     */
    public static void w(Object str) {
        if (sIsDebug) {
            printLog(DEFAULT_TAG, str + "", Log.WARN);
        }
    }

    /**
     * 不打印
     *
     * @param str
     */
    public static void nw(Object str) {

    }

    /**
     * 打印error级别的log.
     *
     * @param tag tag标签
     * @param msg 内容
     * @param e   错误对象.
     */
    public static void e(String tag, Object msg) {
        if (sIsDebug) {
            printLog(tag, msg + "", Log.ERROR);
        }
    }

    /**
     * 不打印
     *
     * @param tag
     * @param msg
     */
    public static void ne(String tag, Object msg) {

    }

    /**
     * 打印error级别的log.
     *
     * @param str 内容
     */
    public static void e(Object str) {
        if (sIsDebug) {
            printLog(DEFAULT_TAG, str + "", Log.ERROR);
        }
    }

    /**
     * 不打印
     *
     * @param str
     */
    public static void ne(Object str) {

    }

    /**
     * 打印info级别的log.
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void i(String tag, Object str) {
        if (sIsDebug) {
            printLog(tag, str + "", Log.INFO);
        }
    }

    /**
     * 不打印
     *
     * @param tag
     * @param str
     */
    public static void ni(String tag, Object str) {

    }

    /**
     * 打印info级别的log.
     *
     * @param str 内容
     */
    public static void i(Object str) {
        if (sIsDebug) {
            printLog(DEFAULT_TAG, str + "", Log.INFO);
        }
    }

    /**
     * 不打印
     *
     * @param str
     */
    public static void ni(Object str) {

    }

    /**
     * 打印verbose级别的log.
     *
     * @param tag tag标签
     * @param str 内容
     */
    public static void v(String tag, Object str) {
        if (sIsDebug) {
            printLog(tag, str + "", Log.VERBOSE);
        }
    }

    /**
     * 不打印
     *
     * @param tag
     * @param str
     */
    public static void nv(String tag, Object str) {

    }

    /**
     * 打印verbose级别的log.
     *
     * @param str 内容
     */
    public static void v(Object str) {
        if (sIsDebug) {
            printLog(DEFAULT_TAG, str + "", Log.VERBOSE);
        }
    }

    /**
     * 不打印
     *
     * @param str
     */
    public static void nv(Object str) {

    }

    /**
     * 打印错误日志
     *
     * @param throwable
     */
    public static void print(Throwable throwable) {
        if (sIsDebug && null != throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 打印带行号的log
     *
     * @param tag
     * @param msg
     * @param logLevel
     */
    public static void printLog(String tag, String msg, int logLevel) {
        printLog(tag, msg, logLevel, OUT_LINK);
    }

    /**
     * 日志输出
     *
     * @param tag            标签
     * @param msg            内容
     * @param logLevel       LOG等级
     * @param isOutputTraces 是否输出调用链
     */
    public static void printLog(String tag, String msg, int logLevel, boolean isOutputTraces) {
        Throwable state = new Throwable(msg);
        StackTraceElement[] stackTraces = state.getStackTrace();
        StringBuffer sBu = new StringBuffer();
        if (!isOutputTraces) {
            StackTraceElement stackTrace = stackTraces[3]; // 根据下标定位层级。
            sBu.append("(").append(stackTrace.getFileName()).append(":").append(stackTrace.getLineNumber())
                    .append("):").append(msg);
        } else {
            for (int i = 0; i < stackTraces.length; i++) {
                if (i > 2) {
                    sBu.append(stackTraces[i].toString()).append("\n");
                } else if (i == 2) {
                    StackTraceElement stackTrace = stackTraces[i];
                    sBu.append("(").append(stackTrace.getFileName()).append(":").append(stackTrace.getLineNumber())
                            .append("):").append(msg).append("\n");
                }
            }
        }
        switch (logLevel) {
            case Log.INFO:
                Log.d(tag, sBu.toString());
                break;
            case Log.VERBOSE:
                Log.v(tag, sBu.toString());
                break;
            case Log.DEBUG:
                Log.d(tag, sBu.toString());
                break;
            case Log.WARN:
                Log.w(tag, sBu.toString());
                break;
            case Log.ERROR:
                Log.e(tag, sBu.toString());
                break;
            default:
                Log.d(tag, sBu.toString());
                break;
        }
    }

}
