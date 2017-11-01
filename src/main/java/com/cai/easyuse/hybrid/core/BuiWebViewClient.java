package com.cai.easyuse.hybrid.core;

import com.cai.easyuse.hybrid.BuiWebViewEventListener;
import com.cai.easyuse.util.AppUtils;
import com.cai.easyuse.util.NetUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 页面请求拦截等处理页面
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public final class BuiWebViewClient extends WebViewClient {

    BuiWebViewEventListener wEvent = null;

    public BuiWebViewClient() {
    }

    public BuiWebViewClient(BuiWebViewEventListener event) {
        wEvent = event;
    }

    public void setWebViewEventListener(BuiWebViewEventListener event) {
        wEvent = event;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //        if (!NetUtils.isUrlValid(url)) {
        //            return true;
        //        }
        super.shouldOverrideUrlLoading(view, url);
        if (!NetUtils.isNetworkAvailable(view.getContext()) && wEvent != null) {
            wEvent.onNetInvalid(view);
        }
        if (URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)) {
            if (view != null) {
                view.loadUrl(url);
            }
        } else {
            // deeplink
            AppUtils.appHandleData(view.getContext(), url);
        }
        return true;
    }

    @Override
    public void onPageStarted(final WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (null != wEvent) {
            wEvent.onPageStart(view);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);

        if (wEvent != null) {
            wEvent.onPageLoadingError(view);
        }
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        super.onPageFinished(view, url);

        if (null != wEvent) {
            wEvent.onPageFinished(view);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

}