package com.cai.easyuse.hybrid;

import com.cai.easyuse.hybrid.model.WebFunInfo;

import android.webkit.WebView;

/**
 * 直接获取从网页进行hybrid的数据
 * <p>
 * Created by cailingxiao on 2017/8/16.
 */
public interface BuiWebViewMessageListener {

    /**
     * 网页发送消息时会接收到
     *
     * @param webView
     * @param info
     *
     * @return 返回true标示拦截默认行为，false标示不拦截。
     */
    boolean onWebSendMessage(WebView webView, WebFunInfo info);
}
