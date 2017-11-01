package com.cai.easyuse.hybrid.util;

import com.cai.easyuse.hybrid.model.WebFunInfo;

import android.text.TextUtils;

/**
 * 前端工具
 * <p>
 * Created by cailingxiao on 2017/8/16.
 */
public final class WebUtils {
    private static final String JS = "javascript";

    private WebUtils() {

    }

    /**
     * js简单拼接.
     *
     * @param callbackFun
     * @param actionId
     * @param result
     *
     * @return
     */
    public static String getJs(String callbackFun, String actionId, String result) {
        final String realResult = TextUtils.isEmpty(result) ? "{}" : result;
        return JS + ":" + callbackFun + "(\'" + actionId + "\'," + "\'" + realResult + "\');";
    }

    /**
     * js简单拼接
     *
     * @param info
     * @param result
     *
     * @return
     */
    public static String getJs(WebFunInfo info, String result) {
        return getJs(info.webCallbackFunc, info.webCallbackActionId, result);
    }
}
