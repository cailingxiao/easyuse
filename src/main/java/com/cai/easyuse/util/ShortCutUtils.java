package com.cai.easyuse.util;

import com.cai.easyuse.R;

import android.content.Context;
import android.content.Intent;

/** 
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年4月17日 下午9:27:36 
 * 类说明 
 */

/**
 * 产生快捷方式的工具类
 * 
 * @author cailingxiao
 * @date 2016年4月17日
 */
public final class ShortCutUtils {

    private static final String ACTION_INSTALL_SHORTCUT =
            "com.android.launcher.action.INSTALL_SHORTCUT";

    /**
     * 是否可以有多个快捷方式的副本
     */
    private static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

    private ShortCutUtils() {

    }

    /**
     * 创建缩略图 需要权限<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
     * 
     * @param icon
     */
    public static void createShortCut(Context ctx, int icon) {
        Intent shortcutIntent = new Intent(ACTION_INSTALL_SHORTCUT);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                ResUtils.getString(R.string.app_name));
        shortcutIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);

        Intent intent2 = new Intent(Intent.ACTION_MAIN);
        intent2.addCategory(Intent.CATEGORY_LAUNCHER);
        intent2.setClass(ctx, ctx.getClass());

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
        shortcutIntent.putExtra(
                Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(
                        ContextUtils.getContext(), icon)); // 可以修改icon的值
        ctx.sendBroadcast(shortcutIntent);
    }

    /**
     * 删除快捷方式
     * <p>
     * 需要权限 <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT"/>
     * */
    public static void deleteShortCut(Context context) {
        Intent shortcut =
                new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                context.getString(R.string.app_name));
        /** 删除和创建需要对应才能找到快捷方式并成功删除 **/
        Intent intent = new Intent();
        intent.setClass(context, context.getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        context.sendBroadcast(shortcut);
    }
}
