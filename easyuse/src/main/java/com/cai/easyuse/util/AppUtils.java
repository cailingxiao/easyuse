package com.cai.easyuse.util;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

/**
 * app版本名、版本号等信息;其他app相关信息
 *
 * @author cailingxiao
 * @date 2016年2月25日
 */
public final class AppUtils {
    private static final String TAG = "AppUtils";

    private AppUtils() {

    }

    /**
     * 获取应用版本名称.
     *
     * @param context 上下文
     *
     * @return 应用版本名
     */
    public static String getAppVersionName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (null != pi) {
            return pi.versionName;
        }
        return "1.0.0";
    }

    /**
     * 获取应用版本号。
     *
     * @param context 上下文
     *
     * @return 应用版本号
     */
    public static int getAppVersionCode(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (null != pi) {
            return pi.versionCode;
        }
        return 1;
    }

    /**
     * 获取应用包名
     *
     * @param context
     *
     * @return
     */
    public static String getAppPackageName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (null != pi) {
            return pi.packageName;
        }
        return "";
    }

    /**
     * 获取应用名
     *
     * @param context
     *
     * @return
     */
    public static String getAppName(Context context) {
        PackageInfo pi = getPackageInfo(context);
        if (null != pi) {
            return context.getPackageManager().getApplicationLabel(pi.applicationInfo) + "";
        }
        return "";
    }

    /**
     * 获取应用的包信息
     *
     * @param context
     *
     * @return
     */
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        if (null == context) {
            return null;
        }
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    /**
     * 手机上是否已经安装了某个应用
     *
     * @param packageName
     *
     * @return
     */
    public static boolean isAvilibleApp(String packageName) {
        final PackageManager packageManager = ContextUtils.getContext().getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取外部未安装apk的包信息
     *
     * @param apkFile
     *
     * @return
     */
    public static PackageInfo getApkFilePkgInfo(Context context, File apkFile) {
        PackageManager pm = null;
        try {
            pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
            return pi;
        } catch (Exception ex) {
            LogUtils.e(TAG, ex.getMessage());
        }
        return null;
    }

    /**
     * 是否有app可以处理这个data
     *
     * @param ctx
     * @param data
     *
     * @return
     */
    public static boolean canAppHandledData(Context ctx, String data) {
        if (null != ctx && !TextUtils.isEmpty(data)) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(data));
                return canAppHandledData(ctx, i);
            } catch (Exception e) {
                LogUtils.print(e);
            }
        }
        return false;
    }

    /**
     * 是否有app可以处理这个data
     *
     * @param ctx
     * @param i
     *
     * @return
     */
    public static boolean canAppHandledData(Context ctx, Intent i) {
        if (null != ctx && null != i) {
            try {
                PackageManager pm = ctx.getPackageManager();
                List<ResolveInfo> handles = pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
                return null != handles && 0 < handles.size();
            } catch (Exception e) {
                LogUtils.print(e);
            }
        }
        return false;
    }

    /**
     * 其他应用处理这个数据
     *
     * @param context
     * @param data
     *
     * @return
     */
    public static boolean appHandleData(Context context, String data) {
        if (null != context && !TextUtils.isEmpty(data)) {
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(data));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                return true;
            } catch (Exception e) {
                LogUtils.print(e);
            }
        }
        return false;
    }
}
