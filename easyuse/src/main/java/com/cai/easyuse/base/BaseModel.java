package com.cai.easyuse.base;

import android.support.annotation.WorkerThread;

import com.cai.easyuse.http.HttpApi;
import com.cai.easyuse.json.JsonApi;
import com.cai.easyuse.util.FileUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

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


    /**
     * get方法直接获取bean
     *
     * @param url
     * @param <T>
     * @return
     */
    @WorkerThread
    protected <T> T getEntity(String url, Class<T> clazz) throws Exception {
        String jsonStr = getFromNet(url);
        T t = JsonApi.parseObject(jsonStr, clazz);
        return t;
    }

    /**
     * post方法直接获取bean
     *
     * @param url
     * @param clazz
     * @param params
     * @param <T>
     * @return
     */
    @WorkerThread
    protected <T> T postEntity(String url, Class<T> clazz, Map<String, String> params) throws Exception {
        String jsonStr = postFromNet(url, params);
        T t = JsonApi.parseObject(jsonStr, clazz);
        return t;
    }

}
