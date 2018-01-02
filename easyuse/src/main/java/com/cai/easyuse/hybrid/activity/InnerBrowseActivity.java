package com.cai.easyuse.hybrid.activity;

import java.net.MalformedURLException;
import java.net.URL;

import com.cai.easyuse.R;
import com.cai.easyuse.app.BuiActivity;
import com.cai.easyuse.hybrid.BuiWebViewEventListener;
import com.cai.easyuse.hybrid.view.BuiWebView;
import com.cai.easyuse.util.ClickUtils;
import com.cai.easyuse.util.ResUtils;
import com.cai.easyuse.util.ViewsUtils;
import com.cai.easyuse.widget.pull2refresh.PullToRefreshBase;
import com.cai.easyuse.widget.pull2refresh.PullToRefreshScrollView;
import com.cai.easyuse.widget.title.TitleLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;

/**
 * 默认的内部浏览器
 * <p>
 * Created by cailingxiao on 2017/8/16.
 */
public class InnerBrowseActivity extends BuiActivity {
    public static final String PARAMS_URL = "params_url";

    private static final int BACK_GAP = 500;

    private TitleLayout mTitle;
    private BuiWebView mBrowser;
    private View mError;

    private String mOriginUrl = null;

    /**
     * 打开内置浏览器
     *
     * @param context
     * @param url
     *
     * @return
     */
    public static boolean openBrowse(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent intent = new Intent(context, InnerBrowseActivity.class);
            intent.putExtra(PARAMS_URL, url);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inner_browser;
    }

    @Override
    protected void initView() {
        mTitle = find(R.id.aib_title);
        mBrowser = find(R.id.aib_webbrowser);
        mError = find(R.id.aib_error);

        ViewsUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBrowser.reload();
            }
        }, mError);

        mBrowser.setWebViewEventListener(new BuiWebViewEventListener() {
            @Override
            public void onNetInvalid(WebView webView) {
                ViewsUtils.gone(mBrowser);
                ViewsUtils.visible(mError);
            }

            @Override
            public void onPageLoadingError(WebView webView) {
                ViewsUtils.gone(mBrowser);
                ViewsUtils.visible(mError);
            }

            @Override
            public void onPageStart(WebView webView) {
                mTitle.showLoading();
            }

            @Override
            public void onPageFinished(WebView webView) {
                mTitle.hideLoading();
                updateTitle();
            }

            @Override
            public void onPageProgressChange(WebView webView, int newProgress) {

            }
        });

        mTitle.titleBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (ClickUtils.clickInner(BACK_GAP)) {
            mBrowser.clearHistory();
            loadUrl(mOriginUrl);
        } else {
            if (mBrowser.canGoBack()) {
                mBrowser.goBack();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    @Override
    protected void initData() {
        mOriginUrl = getIntent().getStringExtra(PARAMS_URL);
        if (TextUtils.isEmpty(mOriginUrl)) {
            mOriginUrl = getIntent().getDataString();
        }
        loadUrl(mOriginUrl);

    }

    private void updatePullHead(String head) {
        String str = String.format(ResUtils.getString(R.string.f_net_powered_by_x), head);

    }

    private void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            ViewsUtils.visible(mBrowser);
            mBrowser.loadUrl(url);
            ViewsUtils.gone(mError);
        } else {
            ViewsUtils.gone(mBrowser);
            ViewsUtils.visible(mError);
        }

    }

    private void updateTitle() {
        mTitle.titleCenterTxt(mBrowser.getTitle());
        String url = mBrowser.getUrl();
        String host = url;
        if (!TextUtils.isEmpty(host)) {
            try {
                host = new URL(url).getHost();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                host = url;
            }
        }
        updatePullHead(host);
    }
}
