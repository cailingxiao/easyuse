package com.cai.easyuse.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * 上下文工具类
 *
 * @author cailingxiao
 * @date 2016年5月17日
 */
public final class ContextUtils {
    private static Context sContext;

    private ContextUtils() {

    }

    /**
     * 设置缓存的上下文
     *
     * @param context
     */
    public static void setContext(Application context) {
        if (null == context) {
            throw new IllegalStateException("can't set null!");
        }
        sContext = context;
    }

    /**
     * 获取application上下文
     *
     * @return
     */
    public static Context getContext() {
        Context context = sContext;
        if (null == context) {
            throw new IllegalStateException("need call setContext first.");
        }
        return context;
    }

    /**
     * 判断intent是否可用
     *
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Intent intent) {
        final PackageManager packageManager = getContext().getPackageManager();
        List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return null != infos && infos.size() > 0;
    }

    /**
     * 检测当前进程是否是和App一致，针对多进程
     *
     * @return
     */
    public static String getCurrentProcess() {
        int pid = android.os.Process.myPid();
        return getProcessName(pid);
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcess(Context context) {
        try {
            int pid = Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (mActivityManager != null) {
                List processLists = mActivityManager.getRunningAppProcesses();
                if (null != processLists) {
                    Iterator iterator = mActivityManager.getRunningAppProcesses().iterator();
                    while (iterator.hasNext()) {
                        ActivityManager.RunningAppProcessInfo appProcess =
                                (ActivityManager.RunningAppProcessInfo) iterator.next();
                        if (appProcess.pid == pid) {
                            return appProcess.processName;
                        }
                    }
                }
            }
        } catch (Exception var6) {
        }
        return getCurrentProcess();
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
