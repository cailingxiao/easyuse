package com.cai.easyuse.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕尺寸等信息，如果要兼容低分辨率，需要在manifest文件中加入下面的声明
 * <p>
 * {@code <supports-screens
 * android:smallScreens="true"
 * android:normalScreens="true"
 * android:largeScreens="true"
 * android:resizeable="true"
 * android:anyDensity="true" />
 * }
 *
 * @author cailingxiao
 * @date 2016年2月19日
 */
public final class ScreenUtils {
    /**
     * 手机屏幕的宽度
     */
    private static int sScreenWidth = 0;
    /**
     * 手机屏幕的高度
     */
    private static int sScreenHeight = 0;
    /**
     * 手机屏幕的屏幕密度（0.75/1.0/1.5/2.0/3.0/4.0）
     */
    private static float sScreenDensity = -1f;
    /**
     * 手机屏幕的屏幕密度dpi（120/160/240/320/480/640）
     */
    private static int sScreenDensityDpi = 0;

    /**
     * 手机屏幕的状态栏高度
     */
    private static int sStatusBarHeight = -1;

    private ScreenUtils() {

    }

    /**
     * 在横竖屏切换时需要调用，重制
     */
    public static void refresh() {
        sScreenDensity = -1;
        sScreenHeight = -1;
        sScreenWidth = -1;
        sScreenDensityDpi = -1;
        sStatusBarHeight = -1;

    }

    /**
     * 获取windowManager管理类
     *
     * @param context
     *
     * @return
     */
    public static WindowManager getWindowManager(Context context) {
        if (null != context) {
            return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        } else {
            return null;
        }
    }

    /**
     * 获取默认显示度量
     *
     * @param context
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager wm = getWindowManager(context);
        DisplayMetrics metrics = new DisplayMetrics();
        if (null != wm) {
            wm.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics;
    }

    /**
     * 强制使用横屏尺寸
     */
    public static void forceUseLandScreen(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        int w = dm.widthPixels;
        int h = dm.heightPixels;
        sScreenWidth = Math.max(w, h);
        sScreenHeight = Math.min(w, h);
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     *
     * @return 像素为单位
     */
    public static int getScreenWidth(Context context) {
        if (sScreenWidth < 10) {
            initScreen(getDisplayMetrics(context));
        }
        return sScreenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     *
     * @return 像素为单位
     */
    public static int getScreenHeight(Context context) {
        if (sScreenHeight < 10) {
            initScreen(getDisplayMetrics(context));
        }
        return sScreenHeight;
    }

    /**
     * 获取屏幕密度（0.75/1.0/1.5/2.0/3.0/4.0）
     *
     * @param context
     *
     * @return
     */
    public static float getScreenDensity(Context context) {
        if (0 >= sScreenDensity) {
            initScreen(getDisplayMetrics(context));
        }
        return sScreenDensity;
    }

    /**
     * 获取屏幕密度的dpi（120/160/240/320/480/640）
     *
     * @param context
     *
     * @return
     */
    public static int getScreenDensityDpi(Context context) {
        if (0 == sScreenDensityDpi) {
            initScreen(getDisplayMetrics(context));
        }
        return sScreenDensityDpi;
    }

    /**
     * 初始化
     *
     * @param metrics
     */
    private static void initScreen(DisplayMetrics metrics) {
        if (null != metrics) {
            sScreenWidth = metrics.widthPixels;
            sScreenHeight = metrics.heightPixels;
            sScreenDensity = metrics.density;
            sScreenDensityDpi = metrics.densityDpi;
        }
    }

    /**
     * dip转px。
     *
     * @param context 上下文
     * @param pxValue dip
     *
     * @return px
     */
    public static int dip2px(Context context, float pxValue) {
        if (0 >= sScreenDensity) {
            initScreen(getDisplayMetrics(context));
        }
        return Math.round(pxValue * sScreenDensity);
    }

    /**
     * px转 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (0 >= sScreenDensity) {
            initScreen(getDisplayMetrics(context));
        }
        return (int) (pxValue / sScreenDensity + 0.5f);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     *
     * @return
     */
    public static int getStatusHeight(Context context) {
        if (-1 != sStatusBarHeight) {
            return sStatusBarHeight;
        }
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            sStatusBarHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            sStatusBarHeight = dip2px(context, 10);
        }
        return sStatusBarHeight;
    }

}
