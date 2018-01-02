
package com.cai.easyuse.http;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.cai.easyuse.base.BasePresenter;
import com.cai.easyuse.http.base.DownloadCallback;
import com.cai.easyuse.util.CloseUtils;
import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.ConvertUtils;
import com.cai.easyuse.util.LogUtils;
import com.cai.easyuse.util.Md5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络库,包含同步get，同步post，同步文件上传，同步文件下载
 * <p/>
 * Created by cailingxiao on 2017/1/13.
 */
public final class HttpApi extends BasePresenter {
    private static final String TAG = "HttpApi";
    private static final boolean DEBUG = false;
    /**
     * 自动重试最大次数
     */
    private static final int MAX_RETRY_TIMES = 3;
    /**
     * 每次下载的block大小
     */
    private static final int BLOCK_SIZE = 1024 * 50;  // 50KB
    private static final String BOUNDARY = "xx--------------------------------------------------------------xx";
    private static volatile HttpApi sInstance = null;
    /**
     * 下载的临时目录,下载文件时会先将文件下载到此处
     */
    private File mDownloadTmpDir = null;
    /**
     * 网络请求client
     */
    private OkHttpClient mClient = null;

    /**
     * 网络请求重试机制,url->times
     */
    private Map<String, Integer> mRetryPolicy = new ConcurrentHashMap<String, Integer>();

    /**
     * 正在下载的文件url
     */
    private Map<String, File> mDownloadingUrls = new ConcurrentHashMap<String, File>();

    /**
     * 文件下载的回调列表
     */
    private Map<String, List<DownloadCallback>> mDownloadCallbacks = new ConcurrentHashMap<>();

    private HttpApi() {
        mClient = new OkHttpClient();
        mDownloadTmpDir = ContextUtils.getContext().getExternalCacheDir();
        if (null != mDownloadTmpDir && !mDownloadTmpDir.exists()) {
            mDownloadTmpDir.mkdirs();
        }
    }

    public static HttpApi getInstance() {
        if (null == sInstance) {
            synchronized (HttpApi.class) {
                if (null == sInstance) {
                    sInstance = new HttpApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 同步获取，推荐将参数encode后传入
     *
     * @param url
     * @return
     */
    @WorkerThread
    public String syncGet(String url) throws IOException {
        Request.Builder builder = new Request.Builder();
        //        builder.cacheControl(createCacheControl());
        Request request = builder.url(url).build();
        Call call = mClient.newCall(request);
        try {
            Response response = call.execute();
            if (null != response && response.body() != null && response.code() == 200) {
                // 请求成功
                mRetryPolicy.remove(url);
                return response.body().string();
            } else if (null != response && 300 <= response.code() && 307 >= response.code()) {
                // 重定向
                String realUrl = response.header("Location");
                if (!TextUtils.isEmpty(realUrl)) {
                    return syncGet(realUrl);
                }
                // 到这里是重定向失败，不进行重试
            } else {
                // 请求失败
                int times = ConvertUtils.toInt(mRetryPolicy.get(url));
                if (times >= MAX_RETRY_TIMES) {
                    mRetryPolicy.remove(url);
                    return null;
                } else {
                    mRetryPolicy.put(url, times + 1);
                    LogUtils.e(TAG, "retry times=" + (times + 1) + url);
                    return syncGet(url);
                }
            }
        } catch (IOException ex) {
            if (!TextUtils.isEmpty(url)) {
                int times = ConvertUtils.toInt(mRetryPolicy.get(url));
                if (times >= MAX_RETRY_TIMES) {
                    mRetryPolicy.remove(url);
                    throw ex;
                } else {
                    mRetryPolicy.put(url, times + 1);
                    LogUtils.e(TAG, "retry times=" + (times + 1) + url);
                    return syncGet(url);
                }
            }
        }
        return null;
    }


    /**
     * 同步post请求，里面不做encode处理
     *
     * @param url
     * @param params 推荐encode后的key和value
     * @return
     */
    @WorkerThread
    public String syncPost(String url, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder();
        //        builder.cacheControl(createCacheControl());
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (null != params && params.size() > 0) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                formBuilder.add(key, value + "");
            }
        }
        Request request = builder.url(url).post(formBuilder.build()).build();
        Call call = mClient.newCall(request);
        try {
            Response response = call.execute();
            if (null != response && null != response.body() && 200 == response.code()) {
                // 请求成功
                mRetryPolicy.remove(url);
                return response.body().string();
            } else if (null != response && 300 <= response.code() && 307 >= response.code()) {
                // 重定向
                String realUrl = response.header("Location");
                if (!TextUtils.isEmpty(realUrl)) {
                    return syncPost(realUrl, params);
                }
                // 到这里是重定向失败，不进行重试
            } else {
                // 请求失败
                int times = ConvertUtils.toInt(mRetryPolicy.get(url));
                if (times >= MAX_RETRY_TIMES) {
                    mRetryPolicy.remove(url);
                    return null;
                } else {
                    mRetryPolicy.put(url, times + 1);
                    LogUtils.e(TAG, "retry times=" + (times + 1) + url);
                    return syncPost(url, params);
                }
            }
        } catch (IOException ex) {
            if (!TextUtils.isEmpty(url)) {
                int times = ConvertUtils.toInt(mRetryPolicy.get(url));
                if (times >= MAX_RETRY_TIMES) {
                    mRetryPolicy.remove(url);
                    throw ex;
                } else {
                    mRetryPolicy.put(url, times + 1);
                    LogUtils.e(TAG, "retry times=" + (times + 1) + url);
                    return syncPost(url, params);
                }
            }
        }
        return null;
    }

    /**
     * 不带进度的下载
     *
     * @param url
     * @return
     * @throws IOException
     */
    @WorkerThread
    public File syncDownload(String url) throws IOException {
        return syncDownload(url, null);
    }


    /**
     * 同步下载文件，文件下载成功后返回临时文件地址
     *
     * @param url
     * @return 返回下载的文件
     */
    @WorkerThread
    public File syncDownload(final String url, final DownloadCallback callback) throws IOException {
        addNewDownloadUrl(url, callback);
        if (!TextUtils.isEmpty(url) && mDownloadingUrls.containsKey(url)) {
            return null;
        }
        // retry policy
        if (!mRetryPolicy.containsKey(url)) {
            mRetryPolicy.put(url, 0);
        }

        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).build();
        Call call = mClient.newCall(request);
        Response response = call.execute();
        List<DownloadCallback> ds = null;
        if (null != response && null != response.body() && HttpURLConnection.HTTP_OK == response.code
                ()) {
            byte[] blocks = new byte[BLOCK_SIZE];
            InputStream inputStream = response.body().byteStream();
            File tmpFile = createTmpFile(url, mDownloadTmpDir);
            mDownloadingUrls.put(url, tmpFile);
            OutputStream outputStream = new FileOutputStream(tmpFile);
            int len = -1;
            long current = 0;
            final long total = ConvertUtils.str2Long(response.header("content-length", "-1"));
            ds = mDownloadCallbacks.get(url);
            if (null != ds) {
                for (DownloadCallback cal : ds) {
                    cal.onDownloadStart(url);
                }
            }
            if (DEBUG) {
                LogUtils.e(TAG, "file downloading...");
            }
            ds = mDownloadCallbacks.get(url);
            while ((len = inputStream.read(blocks)) > 0) {
                outputStream.write(blocks, 0, len);
                current += len;
                if (null != ds) {
                    for (DownloadCallback cal : ds) {
                        cal.onDownloading(url, current, total);
                    }
                }
                if (DEBUG) {
                    LogUtils.e(TAG, "file downloading..." + url + "," + current * 1.0f / total);
                }
            }
            if (DEBUG) {
                LogUtils.e(TAG, "file over..." + len);
            }
            CloseUtils.closeWithFlush(outputStream);
            ds = mDownloadCallbacks.get(url);
            if (null != ds) {
                for (DownloadCallback cal : ds) {
                    cal.onDownloadFinished(url, tmpFile);
                }
            }
            mDownloadingUrls.remove(url);
            mDownloadCallbacks.remove(url);
            return tmpFile;
        } else {
            mDownloadingUrls.remove(url);
            mDownloadCallbacks.remove(url);
            // retry policy
            int count = mRetryPolicy.get(url);
            LogUtils.e(TAG, "file download retry...nowCount=" + count);
            if (count < MAX_RETRY_TIMES) {
                count++;
                mRetryPolicy.put(url, count);
                return syncDownload(url, callback);
            } else {
                mRetryPolicy.remove(url);
            }
        }

        return null;
    }

    /**
     * 同步上传文件到服务器
     *
     * @param url
     * @param file
     * @param data
     * @return
     * @throws IOException
     */
    @WorkerThread
    public String syncUpload(String url, File file, Map<String, String> data) throws IOException {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder(BOUNDARY);
        bodyBuilder.setType(MultipartBody.FORM);
        if (null != data && data.size() > 0) {
            for (String key : data.keySet()) {
                String value = data.get(key);
                bodyBuilder.addFormDataPart(key, value);
            }
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        bodyBuilder.addFormDataPart("file", file.getName(), fileBody);

        Request.Builder reqBuilder = new Request.Builder();
        Request request = reqBuilder.url(url).post(bodyBuilder.build()).build();
        Call call = mClient.newCall(request);
        Response response = call.execute();
        if (null != response && null != response.body() && HttpURLConnection.HTTP_OK == response.code()) {
            return response.body().string();
        }
        return null;
    }

    /**
     * 创建下载的临时文件
     *
     * @param url
     * @param dir
     * @return
     */
    private File createTmpFile(String url, File dir) {
        File tmpFile = new File(dir, Md5Utils.md5(url) + ".tmp");
        return tmpFile;
    }

    private void addNewDownloadUrl(String url, DownloadCallback callback) {
        if (!TextUtils.isEmpty(url) && null != callback) {
            List<DownloadCallback> list = mDownloadCallbacks.get(url);
            if (null == list) {
                list = new ArrayList<>();
            }
            list.add(callback);
            mDownloadCallbacks.put(url, list);
        }
    }

    /**
     * 不使用缓存，全部走网络
     *
     * @return
     */
    public static CacheControl createCacheControl() {
        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.noCache(); // 不使用缓存，全部走网络
        return cacheBuilder.build();
    }

}
