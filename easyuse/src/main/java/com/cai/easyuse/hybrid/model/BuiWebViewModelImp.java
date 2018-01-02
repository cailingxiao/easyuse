package com.cai.easyuse.hybrid.model;

import java.lang.ref.WeakReference;

import org.json.JSONException;
import org.json.JSONObject;

import com.cai.easyuse.base.BasePresenter;
import com.cai.easyuse.hybrid.BuiWebViewMessageListener;
import com.cai.easyuse.hybrid.constant.BuiWebConstant;
import com.cai.easyuse.hybrid.core.BuiInvokeManager;
import com.cai.easyuse.hybrid.util.WebUtils;
import com.cai.easyuse.util.LogUtils;

import android.text.TextUtils;
import android.webkit.JsPromptResult;
import android.webkit.WebView;

/**
 * 默认的webview处理类
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public class BuiWebViewModelImp extends BasePresenter implements BuiWebViewModel {
    private static final String TAG = "BuiWebViewModeImp";

    public BuiWebViewModelImp() {

    }

    @Override
    public void handle(final WebView view, String url, String message, String defaultValue, BuiWebViewMessageListener
            callback) {
        if (null != view && !TextUtils.isEmpty(message)) {
            execute(new JsTaskRunnable(view, message, callback));
        }
    }

    /**
     * 解析message并处理的类，运行在线程池中
     *
     * @author cailingxiao
     * @date 2016年3月3日
     */
    private static class JsTaskRunnable implements Runnable {
        private WeakReference<WebView> mRef;
        private WeakReference<BuiWebViewMessageListener> mListenRef;
        private String mMsg;

        public JsTaskRunnable(WebView webView, String msg, BuiWebViewMessageListener listener) {
            if (null != webView) {
                mRef = new WeakReference<WebView>(webView);
            }
            if (null != listener) {
                mListenRef = new WeakReference<BuiWebViewMessageListener>(listener);
            }
            mMsg = msg;
        }

        @Override
        public void run() {
            if (null == mRef) {
                return;
            }
            final WebView view = mRef.get();
            if (TextUtils.isEmpty(mMsg) || null == view) {
                return;
            }
            JSONObject jsonReq = null;
            try {
                jsonReq = new JSONObject(mMsg);
            } catch (JSONException e) {
                e.printStackTrace();
                jsonReq = null;
            }
            if (null == jsonReq) {
                return;
            }
            String callbackFun = jsonReq.optString(BuiWebConstant.JSON_CALLBACK, "");
            String actionId = jsonReq.optString(BuiWebConstant.JSON_ACTIONID, "");
            String method = jsonReq.optString(BuiWebConstant.JSON_METHOD, "");
            JSONObject argsObj = jsonReq.optJSONObject(BuiWebConstant.JSON_ARGS);
            if (null == argsObj && jsonReq.has(BuiWebConstant.JSON_ARGS)) {
                String strArgs = jsonReq.optString(BuiWebConstant.JSON_ARGS, "");
                if (!TextUtils.isEmpty(strArgs)) {
                    try {
                        argsObj = new JSONObject(strArgs);
                    } catch (JSONException e) {
                        LogUtils.d(TAG, e.getMessage());
                    }
                }
            }

            WebFunInfo info = new WebFunInfo();
            info.webCallbackFunc = callbackFun;
            info.webCallbackActionId = actionId;
            info.naCallMethod = method;
            info.naCallArgs = argsObj;

            if (null != mListenRef && null != mListenRef.get() && null != mRef && null != mRef.get()) {
                boolean intercept = mListenRef.get().onWebSendMessage(mRef.get(), info);
                if (intercept) {
                    return;
                }
            }

            // 进行功能处理，result是返回的结果
            String result = BuiInvokeManager.getInstance().callNativeFun(view.getContext(), method, argsObj);
            if (!TextUtils.isEmpty(callbackFun)) {
                // 获得注入字符串
                String injectStr = WebUtils.getJs(callbackFun, actionId, result);
                // 发起注入结果
                if (null != view) {
                    view.post(new InjectRunnable(view, injectStr));
                }
            }
        }
    }

    /**
     * 向webview注入代码得runnable，运行在主线程中
     *
     * @author cailingxiao
     * @date 2016年3月3日
     */
    private static class InjectRunnable implements Runnable {
        private WeakReference<WebView> mRef;
        private String mResult;

        public InjectRunnable(WebView webView, String result) {
            if (null != webView) {
                mRef = new WeakReference<WebView>(webView);
            }
            mResult = result;
        }

        @Override
        public void run() {
            if (!TextUtils.isEmpty(mResult) && mRef.get() != null) {
                try {
                    mRef.get().loadUrl(mResult);
                } catch (Throwable ignored) {
                    LogUtils.d(TAG, "inject failed!");
                }
            }
        }

    }
}
