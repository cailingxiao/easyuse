package com.cai.easyuse.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.util.UUID;

/**
 * 设备相关的工具类，可以获取imei，cpu架构，处理器数量，内存数等
 *
 * @author cailingxiao
 * @date 2016年2月19日
 */
public final class DeviceUtils {

    /**
     * 没有serialId的设备使用这个
     */
    private static final String PHONE_ID_SHORT = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
            Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
            Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
            Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
            Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
            Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
            Build.USER.length() % 10;
    private static CPU sCPUType = CPU.UNDEFINED; // 默认未指定cpu架构
    /**
     * cache 设备唯一编码
     */
    private static String sPhoneUniqueId = null;

    private DeviceUtils() {

    }

    /**
     * 沉浸状态栏之前调用
     *
     * @param on
     * @param activity
     */
    @TargetApi(19)
    public static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 获取设备机型名.
     *
     * @return 设备机型字符串
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * 获取手机制造商
     *
     * @return
     */
    public static String getDeviceVendor() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取系统版本.
     *
     * @return 当前版本
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取设备号，即imei号。
     * <p>
     * Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context 上下文
     * @return 设备ID, 如果没有权限，会返回00000000000
     */
    public static String getDeviceId(Context context) {
        if (!PermissionUtils.hasPermission(context, "android.permission.READ_PHONE_STATE")) {
            Log.e("DeviceUtils", "read_phone_state permission denied");
            return "";
        }
        String imeiNum = "";
        if (null != context) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (null != tm) {
                try {
                    imeiNum = tm.getDeviceId();
                } catch (Throwable ignored) {
                    imeiNum = "000000000000000";
                }
            }
        }

        return imeiNum;
    }

    /**
     * 获取当前手机的类型.
     *
     * @return {@link CPU#ARM} 、{@link CPU#X86}、{@link CPU#MIPS}、{@link CPU#UNDEFINED}
     */
    public static CPU getCpuType() {
        // 已经检查过cpu类型，直接返回
        if (sCPUType != CPU.UNDEFINED) {
            return sCPUType;
        }

        CPU result = CPU.UNDEFINED;

        String cpuType = Build.CPU_ABI;

        if (cpuType.indexOf("arm") >= 0) {
            result = CPU.ARM;
        } else if (cpuType.indexOf("x86") >= 0) {
            result = CPU.X86;
        } else if (cpuType.indexOf("mips") >= 0) {
            result = CPU.MIPS;
        }

        // 缓存cpu类型
        sCPUType = result;

        return result;
    }

    /**
     * 获得核心数
     *
     * @return
     */
    public static int getProcessorCount() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取该应用最多可用的内存大小（超过这个值，应用会oom）
     *
     * @return 返回的大小单位为B
     */
    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    /**
     * 获取该应用已经使用的内存大小
     *
     * @return 返回的大小单位为B
     */
    public static long getAlreadyUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * 获取该应用还剩下多少空间可用
     *
     * @return 返回的大小单位为B
     */
    public static long getAvaliableMemory() {
        return getMaxMemory() - getAlreadyUsedMemory();
    }

    /**
     * 获取设备唯一号，这个号码可以唯一确定设备
     *
     * @return
     */
    public static String getPhoneUniqueId() {
        if (!TextUtils.isEmpty(sPhoneUniqueId)) {
            return sPhoneUniqueId;
        }
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            // API>=9 使用serial号
            return new UUID(PHONE_ID_SHORT.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        sPhoneUniqueId = new UUID(PHONE_ID_SHORT.hashCode(), serial.hashCode()).toString();
        return sPhoneUniqueId;
    }

    /**
     * 复制到剪切板
     *
     * @param content
     * @param context
     * @return
     */
    public static boolean copy(String content, Context context) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        // 得到剪贴板管理器
        if (OsVerUtils.hasHoneycomb()) {
            android.content.ClipboardManager cmb =
                    (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
        } else {
            android.text.ClipboardManager clipboardManager =
                    (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(content.trim());
            if (clipboardManager.hasText()) {
                clipboardManager.getText();
            }
        }
        return true;
    }

    /**
     * 获取手机信息，用作调试
     *
     * @param context
     * @return
     */
    public static String[] getPhoneInfo(Context context) {
        String[] rets = new String[9];
        rets[0] = "设备机型名:" + getDeviceName();
        rets[1] = "手机制造商:" + getDeviceVendor();
        rets[2] = "系统版本:" + getSystemVersion();
        rets[3] = "设备号:" + getDeviceId(context);
        rets[4] = "手机CPU类型:" + getCpuType().name();
        rets[5] = "核心数:" + getProcessorCount() + "";
        rets[6] = "MaxMemory:" + getMaxMemory() + "byte";
        rets[7] = "UsedMemory:" + getAlreadyUsedMemory() + "byte";
        rets[8] = "UniqueKey:" + getPhoneUniqueId();
        return rets;
    }

    /**
     * cpu型号
     *
     * @author cailingxiao
     * @date 2016年2月19日
     */
    public enum CPU {
        UNDEFINED, ARM, X86, MIPS
    }
}
