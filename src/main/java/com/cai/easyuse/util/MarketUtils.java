package com.cai.easyuse.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/*
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年4月17日 下午3:27:15 
 * 类说明 
 */

/**
 * 市场评分
 *
 * @author cailingxiao
 * @date 2016年4月17日
 */
public final class MarketUtils {

    private MarketUtils() {

    }

    /**
     * 进行去应用市场评分操作
     */
    public static void mark(Context ctx) {
        Uri uri =
                Uri.parse("market://details?id="
                        + AppUtils.getAppPackageName(ctx));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}
