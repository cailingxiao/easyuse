package com.cai.easyuse.hybrid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cai.easyuse.hybrid.BuiWebViewEventListener;
import com.cai.easyuse.hybrid.BuiWebViewMessageListener;
import com.cai.easyuse.hybrid.core.BuiWebChromeClient;
import com.cai.easyuse.hybrid.core.BuiWebViewClient;
import com.cai.easyuse.util.AppUtils;
import com.cai.easyuse.util.LogUtils;
import com.cai.easyuse.util.OsVerUtils;

import java.lang.reflect.Field;

/**
 * 基本的webview类
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public class BuiWebView extends WebView {
    private static final String TAG = "BuiWebView";

    private static final String DEFAULT_UA = "easyuse-hybrid";
    /**
     * 小于300毫秒连续点2次，为双击
     */
    private static final int DB_CLICK_GAP = 300;
    private static Field sConfigCallback;

    static {
        try {
            sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
            sConfigCallback.setAccessible(true);
        } catch (Exception ignored) {
            LogUtils.print(ignored);
        }

    }

    private long mTouchLastTime; // 上次点击时间
    private boolean mIsAccessDoubleTouch; // 是否允许双击事件
    private BuiWebViewClient mViewClient;
    private BuiWebChromeClient mChromeClient;

    /**
     * 构造函数
     */
    public BuiWebView(Context context) {
        super(context);
        init(context, DEFAULT_UA, null);
    }

    /**
     * 构造函数， 静态添加时调用
     */
    public BuiWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, DEFAULT_UA, null);
    }

    public BuiWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, DEFAULT_UA, null);
    }

    /**
     * 构造函数
     *
     * @param context   当前Context
     * @param userAgent 自定义当前WebView的agent
     * @param event     webView网络回调
     */
    public BuiWebView(Context context, String userAgent, BuiWebViewEventListener event) {
        super(context);
        init(context, userAgent, event);
    }

    /**
     * 状态进度回调
     *
     * @param event
     */
    public void setWebViewEventListener(BuiWebViewEventListener event) {
        if (null != mViewClient) {
            mViewClient.setWebViewEventListener(event);
        }
        if (null != mChromeClient) {
            mChromeClient.setBuiWebViewEventListener(event);
        }

    }

    /**
     * 信息回调
     *
     * @param listener
     */
    public void setWebViewMessageListener(BuiWebViewMessageListener listener) {
        if (null != mChromeClient) {
            mChromeClient.setBuiWebViewMessageListener(listener);
        }
    }

    private void init(Context context, String userAgent, BuiWebViewEventListener event) {
        if (isInEditMode()) {
            return;
        }
        mIsAccessDoubleTouch = false;
        mTouchLastTime = 0;

        mViewClient = new BuiWebViewClient(event);
        mChromeClient = new BuiWebChromeClient();
        mChromeClient.setBuiWebViewEventListener(event);

        setWebViewClient(mViewClient);
        setWebChromeClient(mChromeClient);

        setVerticalScrollBarEnabled(false);
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY); // 取消滚动条
        setAccessDoubleTouch(false);

        requestFocus();// 如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件
        requestFocusFromTouch();

        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                AppUtils.appHandleData(getContext(), url);
            }
        });

        WebSettings webSettings = getSettings();
        initWebSetting(context, webSettings, userAgent);
    }

    /**
     * 默认WebSetting配置
     *
     * @param mContext    当前Context对象
     * @param webSettings
     */
    @SuppressLint("NewApi")
    private void initWebSetting(Context mContext, WebSettings webSettings, String userAgent) {
        webSettings.setJavaScriptEnabled(true); // 开启javascript设置
        webSettings.setAllowFileAccess(true); // 可以读取文件缓存(manifest生效)
        webSettings.setBuiltInZoomControls(false); // 是否显示页面缩放的按钮
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setDefaultTextEncodingName("UTF-8");
        if (OsVerUtils.hasHoneycomb()) {
            webSettings.setAllowContentAccess(true);
        }

        webSettings.setAppCacheEnabled(true); // 应用可以有缓存
        String appCaceDir = mContext.getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCaceDir);
        webSettings.setDomStorageEnabled(true); // 设置可以使用localStoragex`

        webSettings.setDatabaseEnabled(true); // 应用可以有数据库
        String dbPath = mContext.getDir("database", Context.MODE_PRIVATE).getPath();
        try {
            webSettings.setDatabasePath(dbPath);
        } catch (Exception ignored) {
        }

        // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setGeolocationEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false); // 设置可以支持缩放
        webSettings.setUseWideViewPort(true);

        if (TextUtils.isEmpty(userAgent)) {
            webSettings.setUserAgentString(webSettings.getUserAgentString());
        } else {
            webSettings.setUserAgentString(webSettings.getUserAgentString() + " " + userAgent);
        }

        if (OsVerUtils.hasHoneycomb()) {
            removeJavascriptInterface("searchBoxJavaBridge_");
            removeJavascriptInterface("accessibilityTraversal");
            removeJavascriptInterface("accessibility");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsAccessDoubleTouch) {
            return super.onTouchEvent(event);
        }

        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 禁止双击事件
                    long curTime = System.currentTimeMillis();
                    long durTime = curTime - mTouchLastTime;
                    if (durTime < DB_CLICK_GAP) {
                        mTouchLastTime = curTime;
                        return true;
                    } else {
                        mTouchLastTime = curTime;
                    }
                    break;
            }
            return super.onTouchEvent(event);
        } catch (NullPointerException e) {
            return false;
        }

    }

    /**
     * 设置是否允许双击事件，默认关闭
     *
     * @param flag
     */
    public void setAccessDoubleTouch(boolean flag) {
        mIsAccessDoubleTouch = flag;
    }

    @Override
    public void destroy() {
        super.destroy();
        stopLoading();
        setWebViewClient(null);
        setWebChromeClient(null);
        loadUrl("about:blank");
        removeAllViews();
        try {
            if (sConfigCallback != null) {
                sConfigCallback.set(null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // LogUtils.e(TAG, "#dispatchTouchEvent,x=" + ev.getX() + ",y=" + ev.getY() + ",scrollY=" + getScrollY());
        return super.dispatchTouchEvent(ev);
    }
}