package com.cai.easyuse.hybrid.core;

import com.cai.easyuse.hybrid.BuiWebViewEventListener;
import com.cai.easyuse.hybrid.BuiWebViewMessageListener;
import com.cai.easyuse.hybrid.model.BuiWebViewModel;
import com.cai.easyuse.hybrid.model.BuiWebViewModelImp;
import com.cai.easyuse.util.LogUtils;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

/**
 * 进行webview的chromeClient处理的类，主要用来进行消息传递
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public final class BuiWebChromeClient extends WebChromeClient {
    private static final String TAG = "BuiWebChromeClient";
    private static final boolean DEBUG = LogUtils.isDebug();

    public BuiWebViewModel mWebViewModel = null;
    /**
     * 进度事件回调
     */
    private BuiWebViewEventListener mEventListener = null;
    /**
     * 消息传递回调
     */
    private BuiWebViewMessageListener mMessageListener = null;
    private boolean mHandleInvoke = true;

    public BuiWebChromeClient(BuiWebViewModel model, boolean handleInvoke) {
        mWebViewModel = model;
        mHandleInvoke = handleInvoke;
    }

    public BuiWebChromeClient() {
        this(new BuiWebViewModelImp(), true);
    }

    public void setBuiWebViewEventListener(BuiWebViewEventListener listener) {
        mEventListener = listener;
    }

    public void setBuiWebViewMessageListener(BuiWebViewMessageListener listener) {
        mMessageListener = listener;
    }

    /**
     * 设置是否打开H5调用开关
     *
     * @param flag
     */
    public void setHandleH5Invoke(boolean flag) {
        mHandleInvoke = flag;
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(requiredStorage * 2);
    }

    @Override
    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long quota, long estimatedDatabaseSize,
                                        long totalQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(estimatedDatabaseSize * 2);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (null != mEventListener) {
            mEventListener.onPageProgressChange(view, newProgress);
        }
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        if (mHandleInvoke && null != mWebViewModel) {
            if (DEBUG) {
                LogUtils.d(TAG, "H5页面发起一个Prompt请求:" + defaultValue);
            }
            mWebViewModel.handle(view, url, defaultValue, defaultValue, mMessageListener);
            result.confirm();
        } else {
            if (DEBUG) {
                LogUtils.d(TAG, "H5发送一个不拦截的Prompt请求");
            }
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }
        return true;
    }
}