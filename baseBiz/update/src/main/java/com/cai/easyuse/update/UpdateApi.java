package com.cai.easyuse.update;

import android.content.Context;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * 升级模块
 * <p>
 * Created by cailingxiao on 2017/8/23.
 */
public final class UpdateApi {
    private UpdateApi() {

    }

    /**
     * 初始化
     *
     * @param context
     * @param appId
     * @param channel
     * @param isDebug
     */
    public static void init(Context context, String appId, String channel, boolean isDebug) {
        Bugly.setAppChannel(context, channel);
        Bugly.init(context, appId, isDebug);
    }

    /**
     * 检查更新
     *
     * @param isSilence 是否静默检查
     */
    public static void checkUpdate(boolean isSilence) {
        Beta.checkUpgrade(false, isSilence);
    }
}
