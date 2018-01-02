package com.cai.easyuse.hybrid;

import android.webkit.WebView;

/**
 * 监听页面的几种状态
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public interface BuiWebViewEventListener {

    /**
     * 网络无效时调用
     *
     * @param webView
     */
    void onNetInvalid(WebView webView);

    /**
     * 网页读取失败时调用
     *
     * @param webView
     */
    void onPageLoadingError(WebView webView);

    /**
     * 页面开始加载
     *
     * @param webView
     */
    void onPageStart(WebView webView);

    /**
     * 页面加载完成调用
     *
     * @param webView
     */
    void onPageFinished(WebView webView);

    /**
     * 进度变化的回调
     *
     * @param webView
     * @param newProgress
     */
    void onPageProgressChange(WebView webView, int newProgress);

}