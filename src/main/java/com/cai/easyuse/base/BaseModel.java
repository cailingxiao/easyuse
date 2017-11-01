package com.cai.easyuse.base;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.cai.easyuse.http.HttpApi;
import com.cai.easyuse.util.AppUtils;
import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.FileUtils;

import android.support.annotation.WorkerThread;

/**
 * mvp模式下model，处理数据存取，使用同步操作
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public class BaseModel {
    public BaseModel() {

    }

    /**
     * get网络请求
     *
     * @param url
     *
     * @return
     */
    protected String getFromNet(String url) {
        try {
            return HttpApi.getInstance().syncGet(url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * post网络请求
     *
     * @param url
     *
     * @return
     */
    protected String postFromNet(String url, Map<String, String> data) {
        try {
            return HttpApi.getInstance().syncPost(url, data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * app请求，通用参数
     *
     * @return
     */
    protected String getCommonParamsString() {
        return "appVersion=" + AppUtils.getAppVersionName(ContextUtils.getContext());
    }

    /**
     * app请求，通用参数
     *
     * @return
     */
    protected Map<String, String> getCommonParamsMap() {
        Map<String, String> data = new HashMap<>();
        data.put("appVersion", AppUtils.getAppVersionName(ContextUtils.getContext()));
        return data;
    }

    /**
     * 保存缓存
     *
     * @param obj
     * @param fileName
     */
    @WorkerThread
    protected void saveToDisk(File dir, Object obj, String fileName) {
        File dataFile = new File(dir, fileName);
        if (dataFile.exists()) {
            FileUtils.deleteSingleFile(dataFile);
        }
        FileUtils.writeSerilizableObj(obj, dataFile);
    }

    /**
     * 从磁盘导入类
     *
     * @param fileName
     *
     * @return
     */
    @WorkerThread
    protected Serializable loadFromDisk(File dir, String fileName) {
        File dataFile = new File(dir, fileName);
        if (!dataFile.exists()) {
            return null;
        }
        return FileUtils.readSerilizableObj(dataFile);
    }

}
