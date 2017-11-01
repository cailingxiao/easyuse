package com.cai.easyuse.http;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.cai.easyuse.base.BasePresenter;
import com.cai.easyuse.base.mark.BuiCallback;
import com.cai.easyuse.http.base.DownloadCallback;
import com.cai.easyuse.util.LogUtils;

import android.text.TextUtils;

/**
 * 异步httpapi
 * <p>
 * Created by cailingxiao on 2017/1/23.
 */
public final class AsyncHttpApi extends BasePresenter {
    private static final String TAG = "AsyncHttpApi";
    private static AsyncHttpApi sInstance = null;

    private AsyncHttpApi() {

    }

    public static AsyncHttpApi getInstance() {
        if (null == sInstance) {
            synchronized (AsyncHttpApi.class) {
                if (null == sInstance) {
                    sInstance = new AsyncHttpApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 异步get请求
     *
     * @param url
     * @param callback
     */
    public void asyncGet(final String url, final BuiCallback callback) {
        execute(new Runnable() {
            @Override
            public void run() {
                String content = null;
                try {
                    content = HttpApi.getInstance().syncGet(url);
                } catch (IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                if (!TextUtils.isEmpty(content)) {
                    final String value = content;
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onSuccess(0, value);
                            }
                        }
                    });
                } else {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onFail(-1, "net error!");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public void asyncPost(final String url, final Map<String, String> params, final BuiCallback callback) {
        execute(new Runnable() {
            @Override
            public void run() {
                String content = null;
                try {
                    content = HttpApi.getInstance().syncPost(url, params);
                } catch (IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                if (!TextUtils.isEmpty(content)) {
                    final String value = content;
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onSuccess(0, value);
                            }
                        }
                    });
                } else {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onFail(-1, "net error!");
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 异步下载
     *
     * @param url
     * @param callback
     */
    public void asyncDownload(final String url, final DownloadCallback callback) {
        executeDownloadTask(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpApi.getInstance().syncDownload(url, callback);
                } catch (IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
            }
        });
    }

    /**
     * 异步文件上传
     *
     * @param url
     * @param file
     * @param data
     * @param callback
     */
    public void asyncUpload(final String url, final File file, final Map<String, String> data, final BuiCallback
            callback) {
        execute(new Runnable() {
            @Override
            public void run() {
                String content = null;
                try {
                    content = HttpApi.getInstance().syncUpload(url, file, data);
                } catch (IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                if (!TextUtils.isEmpty(content)) {
                    final String value = content;
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onSuccess(0, value);
                            }
                        }
                    });
                } else {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onFail(-1, "net error!");
                            }
                        }
                    });
                }
            }
        });
    }
}
