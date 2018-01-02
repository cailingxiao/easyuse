package com.cai.easyuse.util;

import java.lang.reflect.Method;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;

/** 
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年4月17日 下午9:47:58 
 * 类说明 
 */

/**
 * 设置壁纸的工具类
 * 
 * @author cailingxiao
 * @date 2016年4月17日
 */
public final class WallPaperUtils {
    private static final String TAG = "WallPaperUtils";

    private WallPaperUtils() {

    }

    /**
     * 设置锁屏界面的背景图片
     * 
     * @param ctx
     * @param image
     * @return
     */
    public static boolean setLockWallPaper(Context ctx, Bitmap image) {
        try {
            WallpaperManager mWallManager = WallpaperManager.getInstance(ctx);
            Class class1 = mWallManager.getClass();// 获取类名
            Method setWallPaperMethod =
                    class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);// 获取设置锁屏壁纸的函数
            setWallPaperMethod.invoke(mWallManager, image);// 调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
            return true;
        } catch (Throwable e) {
            LogUtils.e(TAG, e.getMessage());
            return false;
        }
    }

    /**
     * 设置桌面背景图片 *
     * <p>
     * 需要权限 {@link android.Manifest.permission#SET_WALLPAPER}.
     * 
     * @param ctx
     * @param image
     * @return
     */
    public static boolean setWallPaper(Context ctx, Bitmap image) {
        WallpaperManager mWallManager = WallpaperManager.getInstance(ctx);
        try {
            mWallManager.setBitmap(image);
            return true;
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
            return false;
        }
    }
}
