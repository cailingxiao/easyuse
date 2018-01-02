package com.cai.easyuse.hybrid.model;

import com.cai.easyuse.hybrid.BuiWebViewMessageListener;

import android.webkit.JsPromptResult;
import android.webkit.WebView;

/**
 * 网页通信任务处理接口
 * 
 * @author cailingxiao
 * @date 2016年3月3日
 * 
 */
public interface BuiWebViewModel {

    /**
     * 处理方法，负责处理message，并给h5注入结果
     * 
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param msgCallback
     */
    void handle(WebView view, String url, String message, String defaultValue, BuiWebViewMessageListener msgCallback);
}
