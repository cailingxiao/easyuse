package com.cai.easyuse.util;

import android.app.Activity;
import android.content.Intent;

/**
 * 系统相关工具方法，可以进入home页面
 * 
 * @author cailingxiao
 * @date 2016年2月18日
 * 
 */
public final class SysUtils {
    private SysUtils() {

    }

    /**
     * 回到桌面，相当于按下home键
     * 
     * @param activity
     */
    public static void home(final Activity activity) {
        if (null != activity) {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                activity.startActivity(intent);
            } catch (Exception e) {
                activity.finish();
            }
        }
    }
}
