package com.cai.easyuse.util;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * 权限工具
 *
 * @author cailingxiao
 * @date 2016年2月29日
 */
public final class PermissionUtils {

    private PermissionUtils() {

    }

    /**
     * 检查是否存在某个名字的权限
     *
     * @param context
     * @param permissionName
     *
     * @return
     */
    public static boolean hasPermission(Context context, String permissionName) {
        if (null == context) {
            return false;
        }
        return context.checkCallingOrSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 向系统请求某些权限
     *
     * @param activity
     * @param permissionNames
     * @param reqCode
     */
    public static void requestPermissions(Activity activity, String[] permissionNames, int reqCode) {
        if (null == activity) {
            return;
        }
        ActivityCompat.requestPermissions(activity, permissionNames, reqCode);
    }

    public static void requestPermission(Activity activity, String permissionName, int reqCode) {
        requestPermissions(activity, new String[] {permissionName}, reqCode);
    }

    public static void requestPermission(Activity activity, String permissionName) {
        requestPermission(activity, permissionName, -1);
    }

    /**
     * 请求所有需要的权限，如果没有，就请求
     *
     * @param activity
     * @param permissionNames
     * @param req
     */
    public static void requestWhenNoPermissions(Activity activity, String[] permissionNames, int req) {
        if (null == activity || null == permissionNames) {
            return;
        }
        Set<String> needs = new HashSet<>();
        for (String item : permissionNames) {
            if (!hasPermission(activity, item)) {
                needs.add(item);
            }
        }
        if (needs.size() > 0) {
            String[] reqs = needs.toArray(new String[0]);
            requestPermissions(activity, reqs, req);
        }

    }
}
