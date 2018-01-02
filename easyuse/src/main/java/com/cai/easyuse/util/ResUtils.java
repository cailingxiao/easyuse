package com.cai.easyuse.util;

import java.util.Locale;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * 安全获取资源的工具类,主要包括string/color/drawable/bitmap,Asset资源请参考{@link AssetUtils}
 *
 * @author cailingxiao
 * @date 2016年2月7日
 */
public final class ResUtils {
    private static final String TAG = "ResUtils";

    private ResUtils() {

    }

    /**
     * 获取资源Resources
     *
     * @return
     */
    public static final Resources getResources() {
        final Context context = ContextUtils.getContext();
        if (null == context) {
            LogUtils.e(TAG, "BuiApplication or BuiApplication.initWithOutInstance() should be called at least!");
            return null;
        } else {
            return context.getResources();
        }
    }

    /**
     * 返回字符串资源
     *
     * @param strId
     * @return
     */
    public static final String getString(int strId) {
        if (0 == strId) {
            return "";
        }
        Resources res = getResources();
        if (null != res) {
            return res.getString(strId);
        } else {
            return "";
        }
    }

    /**
     * 获取stringArray
     *
     * @param arrayId
     * @return
     */
    public static final String[] getStringArray(int arrayId) {
        if (0 == arrayId) {
            return null;
        }
        Resources res = getResources();
        if (null != res) {
            return res.getStringArray(arrayId);
        } else {
            return null;
        }
    }

    /**
     * 返回指定的drawable资源
     *
     * @param drawId
     * @return
     */
    public static final Drawable getDrawable(int drawId) {
        if (0 == drawId) {
            return null;
        }
        if (OsVerUtils.hasLollipop()) {
            return ContextUtils.getContext().getDrawable(drawId);
        } else {
            Resources res = getResources();
            if (null != res) {
                return res.getDrawable(drawId);
            } else {
                return null;
            }
        }
    }

    /**
     * 获取颜色
     *
     * @param colorId
     * @return
     */
    public static final int getColor(int colorId) {
        if (0 == colorId) {
            return Color.parseColor("#000000");
        }
        if (OsVerUtils.hasM()) {
            return ContextUtils.getContext().getColor(colorId);
        } else {
            Resources res = getResources();
            if (null != res) {
                return res.getColor(colorId);
            } else {
                return Color.parseColor("#000000");
            }
        }
    }


    /**
     * 获取资源图片
     *
     * @param bitmapId
     * @return
     */
    public static final Bitmap getBitmap(int bitmapId) {
        if (0 == bitmapId) {
            return null;
        }
        Resources res = getResources();
        if (null != res) {
            return BitmapFactory.decodeResource(res, bitmapId);
        } else {
            return null;
        }
    }

    /**
     * 切换应用语言，调用完成后需要重启activity等
     *
     * @param locale
     */
    public static final void changeLanguage(Locale locale) {
        Locale now = Locale.getDefault();
        if (!now.getDisplayLanguage().equalsIgnoreCase(locale.getDisplayLanguage())) {
            Locale.setDefault(locale);
            Configuration config = ContextUtils.getContext().getResources().getConfiguration();
            config.locale = locale;
            ContextUtils.getContext().getResources().updateConfiguration(config,
                    ContextUtils.getContext().getResources().getDisplayMetrics());
        }
    }

    /**
     * 通过colorId数组获取颜色数组
     *
     * @param colorIds
     * @return
     */
    public static int[] getColors(int... colorIds) {
        if (null == colorIds || colorIds.length < 1) {
            return null;
        }
        int[] colors = new int[colorIds.length];
        for (int i = 0; i < colorIds.length; i++) {
            colors[i] = getColor(colorIds[i]);
        }
        return colors;
    }

}