package com.cai.easyuse.util;

import android.os.Handler;
import android.os.Looper;

/**
 * 主线程工具，需要使用或者先直接调用{@link com.cai.easyuse.app.BaseApplication#initWithOutInstance}
 *
 * @author cailingxiao
 * @date 2016年2月18日
 */
public final class MainThreadUtils {
    private static final Object sLocked = new Object();
    private static Handler sMainHandler = null;

    private MainThreadUtils() {

    }

    /**
     * 初始化mainHandler
     */
    private static void initMain() {
        if (null == sMainHandler) {
            synchronized (sLocked) {
                if (null == sMainHandler) {
                    sMainHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
    }

    /**
     * 获取ui线程的handler
     *
     * @return
     */
    public static Handler getMainHandler() {
        initMain();
        return sMainHandler;
    }

    /**
     * 判断当前线程是否是主线程
     *
     * @return
     */
    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread().getId() == Thread.currentThread().getId();
    }

    /**
     * 保证任务在主线程中执行任务
     *
     * @param cmd
     */
    public static void post(final Runnable cmd) {
        if (null == cmd) {
            return;
        }
        if (isMainThread()) {
            cmd.run();
        } else {
            getMainHandler().post(new Runnable() {

                @Override
                public void run() {
                    cmd.run();
                }
            });
        }
    }

    /**
     * 执行延时任务
     *
     * @param cmd
     * @param delayMillis
     */
    public static void postDelayed(final Runnable cmd, long delayMillis) {
        if (null == cmd) {
            return;
        }
        getMainHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                cmd.run();
            }
        }, delayMillis);
    }

    /**
     * 删除pedding中的任务
     *
     * @param cmd
     */
    public static void cancel(Runnable cmd) {
        getMainHandler().removeCallbacks(cmd);
    }

}
