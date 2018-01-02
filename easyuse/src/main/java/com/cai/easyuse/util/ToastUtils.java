package com.cai.easyuse.util;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * 对Toast进行管理的类
 *
 * @author cailingxiao
 * @date 2016年2月18日
 */
public final class ToastUtils {

    /**
     * 默认弹出的toast显示位置,可在此直接修改，默认使用系统的
     */
    private static final int DefaultGravity = -1;

    private static WeakReference<Toast> sRef = null;

    private ToastUtils() {

    }

    /**
     * 清除显示的toast
     */
    public static void cancel() {
        MainThreadUtils.post(new Runnable() {
            @Override
            public void run() {
                if (null != sRef && null != sRef.get()) {
                    sRef.get().cancel();
                    sRef = null;
                }
            }
        });
    }

    /**
     * 展示简短的提示
     *
     * @param msg
     */
    public static void showToast(String msg) {
        final Context ctx = ContextUtils.getContext();
        showToast(ctx, msg, false);
    }

    /**
     * 展示toast，根据isLong控制
     *
     * @param msg
     * @param isLong
     */
    public static void showToast(String msg, boolean isLong) {
        final Context ctx = ContextUtils.getContext();
        showToast(ctx, msg, isLong);
    }

    /**
     * 展示简短的提示
     *
     * @param stringId
     */
    public static void showToast(int stringId) {
        final Context ctx = ContextUtils.getContext();
        showToast(ctx, stringId, false);
    }

    /**
     * 显示一个toast
     *
     * @param context  上下文
     * @param stringId 代表要显示的字符串的id
     * @param isLong   是否长显示
     */
    public static void showToast(final Context context, final int stringId, final boolean isLong) {
        if (null == context) {
            return;
        }
        showToast(context, context.getString(stringId), isLong, DefaultGravity);
    }

    /**
     * 同{@link ToastUtils#showToast(Context, String, boolean)},第三个参数是false
     *
     * @param context
     * @param stringId
     */
    public static void showToast(final Context context, final int stringId) {
        showToast(context, stringId, false);
    }

    /**
     * 居中显示toast
     *
     * @param context
     * @param stringId
     * @param isLong
     */
    public static void showCenterToast(final Context context, final int stringId, final boolean isLong) {
        if (null == context) {
            return;
        }
        showToast(context, context.getString(stringId), isLong, Gravity.CENTER);
    }

    /**
     * 居中显示toast
     *
     * @param context
     * @param stringId
     */
    public static void showCenterToast(final Context context, final int stringId) {
        showCenterToast(context, stringId, false);
    }

    /************************************************************************************************/

    /**
     * 显示一个toast
     *
     * @param context 上下文
     * @param text    要显示的文字
     * @param isLong  是否长显示
     */
    public static void showToast(final Context context, final String text, final boolean isLong, final int gravity) {
        if (null == context) {
            return;
        }
        MainThreadUtils.post(new Runnable() {
            public void run() {
                if (null != context) {
                    final Toast toast =
                            Toast.makeText(context.getApplicationContext(), text, isLong ? Toast.LENGTH_LONG
                                    : Toast.LENGTH_SHORT);
                    if (-1 != gravity) {
                        toast.setGravity(gravity, 0, 0);
                    }
                    if (null != sRef && null != sRef.get()) {
                        sRef.get().cancel();
                        sRef = null;
                    }
                    sRef = new WeakReference<Toast>(toast);
                    toast.show();
                }
            }
        });
    }

    /**
     * 同{@link ToastUtils#showToast(Context, String, boolean)}，第三个参数为false
     *
     * @param context
     * @param text
     */
    public static void showToast(final Context context, final String text) {
        showToast(context, text, false, DefaultGravity);
    }

    /**
     * 显示toast
     *
     * @param context
     * @param text
     * @param isLong
     */
    public static void showToast(final Context context, final String text, final boolean isLong) {
        showToast(context, text, isLong, DefaultGravity);
    }

    /**
     * 居中显示toast
     *
     * @param context
     * @param text
     * @param isLong
     */
    public static void showCenterToast(final Context context, final String text, final boolean isLong) {
        showToast(context, text, isLong, Gravity.CENTER);
    }

    /**
     * 同{@link ToastUtils#showCenterToast(Context, String, boolean)}，最后一个参数为false
     *
     * @param context
     * @param text
     */
    public static void showCenterToast(final Context context, final String text) {
        showCenterToast(context, text, false);
    }

    /*******************************************************************************************/
    /**
     * 完全自定义Toast
     *
     * @param context
     * @param layout  自定义Toast的VIEW
     * @param gravity 屏幕显示位置
     * @param xOffset x坐标
     * @param yOffset y坐标
     * @param isLong  是否长显示
     */
    public static void showToast(final Context context, final View layout, final int gravity, final int xOffset,
                                 final int yOffset, final boolean isLong) {
        if (context == null || layout == null) {
            return;
        }
        MainThreadUtils.post(new Runnable() {
            public void run() {
                final Toast toast = new Toast(context.getApplicationContext());
                toast.setGravity(gravity, xOffset, yOffset);
                toast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setView(layout);
                if (null != sRef && null != sRef.get()) {
                    sRef.get().cancel();
                    sRef = null;
                }
                sRef = new WeakReference<Toast>(toast);
                toast.show();
            }
        });
    }

}
