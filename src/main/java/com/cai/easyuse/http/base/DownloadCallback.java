package com.cai.easyuse.http.base;

import java.io.File;

/**
 * 下载进度回调接口
 * <p>
 * Created by cailingxiao on 2017/3/23.
 */
public interface DownloadCallback {
    void onDownloadStart(String url);

    void onDownloading(String url, long current, long total);

    void onDownloadFinished(String url, File file);
}
