package com.cai.easyuse.update.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.cai.easyuse.BuildConfig;
import com.cai.easyuse.config.EasyConfigApi;
import com.cai.easyuse.http.HttpApi;
import com.cai.easyuse.json.JsonApi;
import com.cai.easyuse.update.check.ICheckUpdate;
import com.cai.easyuse.util.AppUtils;
import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.FileUtils;
import com.cai.easyuse.util.SecurityUtils;

import android.content.pm.PackageInfo;
import android.text.TextUtils;

/**
 * 检查更新的实现类
 * <p>
 * Created by cailingxiao on 2017/3/19.
 */
public class CheckUpdateModel {
    private static final String UPDATE_NODE = "updateUrl";
    private static final String DOWNLOAD_DIR_NAME = "lastapp";
    private static final String APK_SUFFIX = ".apk";

    private File mDownloadDir = null;
    private String mUpdateUrl = null;

    public CheckUpdateModel() {
        mUpdateUrl = EasyConfigApi.getInstance().getProp(UPDATE_NODE);
        try {
            mDownloadDir = ContextUtils.getContext().getExternalFilesDir(DOWNLOAD_DIR_NAME);
        } catch (Exception e) {
            mDownloadDir = new File(ContextUtils.getContext().getFilesDir(), DOWNLOAD_DIR_NAME);
        }
        FileUtils.mkdirs(mDownloadDir);
    }

    /**
     * 检查更新，入口函数
     */
    public UpdateEntity checkUpdate() {
        if (!TextUtils.isEmpty(mUpdateUrl)) {
            Map<String, String> data = new HashMap<String, String>();
            data.put("appName", AppUtils.getAppPackageName(ContextUtils.getContext()));
            try {
                String content = HttpApi.getInstance().syncPost(mUpdateUrl, data);
                if (!TextUtils.isEmpty(content)) {
                    return JsonApi.parseObject(content, UpdateEntity.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param entity
     * @param callback
     */
    public File downloadFile(final UpdateEntity entity, final ICheckUpdate callback) throws IOException {
        return HttpApi.getInstance().syncDownload(entity.appPath, callback);
    }

    /**
     * 下载文件,悄悄的下，没有回调
     *
     * @param entity
     */
    public void silentDownload(UpdateEntity entity) throws IOException {
        File file = downloadFile(entity, null);
        File goalFile = new File(mDownloadDir, file.getName() + APK_SUFFIX);
        FileUtils.move(file, goalFile, true);
    }

    /**
     * 检查apk包名和签名是否一致
     *
     * @param file
     */
    public boolean verifyFile(File file) {
        if (!file.exists()) {
            return false;
        }
        PackageInfo info = AppUtils.getApkFilePkgInfo(ContextUtils.getContext(), file);
        if (null != info) {
            String nowAppName = "" + AppUtils.getAppName(ContextUtils.getContext());
            // 校验包名
            if (nowAppName.equals(info.packageName)) {
                if (BuildConfig.DEBUG) {
                    return true;
                } else {
                    String appSign = "" + SecurityUtils.getSignatureByPackname(ContextUtils.getContext(), nowAppName);
                    String downloadAppSign = SecurityUtils.getUninstalledApkSignature(file.getPath());
                    if (appSign.equals(downloadAppSign)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
