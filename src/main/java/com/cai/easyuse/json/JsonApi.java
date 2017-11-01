package com.cai.easyuse.json;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import android.text.TextUtils;

/**
 * 提供json字符串和json对象转换的方法
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public final class JsonApi {
    private JsonApi() {

    }

    /**
     * 将json字符串转化成JSON对应的entity
     *
     * @param jsonTxt
     * @param clazz
     * @param <T>
     *
     * @return
     */
    public static synchronized <T> T parseObject(String jsonTxt, Class<T> clazz) throws JSONException {
        if (TextUtils.isEmpty(jsonTxt)) {
            return null;
        }
        try {
            T t = JSON.parseObject(jsonTxt, clazz);
            return t;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 将entity转换成字符串
     *
     * @param entity
     *
     * @return
     */
    public static synchronized String toJsonString(Object entity) {
        if (null == entity) {
            return "";
        }
        return JSON.toJSONString(entity);
    }

    /**
     * 将json字符串转化成entity数组
     *
     * @param jsonTxt
     * @param clazz
     * @param <T>
     *
     * @return
     */
    public static synchronized <T> List<T> parseArray(String jsonTxt, Class<T> clazz) throws JSONException {
        if (TextUtils.isEmpty(jsonTxt)) {
            return null;
        }
        try {
            List<T> r = JSON.parseArray(jsonTxt, clazz);
            return r;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
