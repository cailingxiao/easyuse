package com.cai.easyuse.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.net.Uri;
import android.text.TextUtils;

/**
 * 编码和解码的工具类
 *
 * @author cailingxiao
 * @date 2016年2月19日
 */
public final class UrlUtils {
    private UrlUtils() {

    }

    /**
     * 使用utf-8进行urlencode
     *
     * @param key
     *
     * @return
     */
    public static String urlEncode(String key) {
        try {
            return URLEncoder.encode(key, "utf-8");
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return key;
    }

    /**
     * 进行urlencode
     *
     * @param key
     * @param charset
     *
     * @return
     */
    public static String urlEncode(String key, String charset) {
        try {
            return URLEncoder.encode(key, charset);
        } catch (UnsupportedEncodingException e) {
            LogUtils.e(e.getMessage(), e);
        }
        return key;
    }

    /**
     * 使用utf-8进行urldecode
     *
     * @param key
     *
     * @return
     */
    public static String urlDecode(String key) {
        try {
            return URLDecoder.decode(key, "utf-8");
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return key;
    }

    /**
     * 进行urldecode
     *
     * @param key
     * @param charset
     *
     * @return
     */
    public static String urlDecode(String key, String charset) {
        try {
            return URLDecoder.decode(key, charset);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return key;
    }

    /**
     * 给url中添加一个键值对
     *
     * @param url
     * @param key
     * @param value
     *
     * @return
     */
    public static String addParam(String url, String key, String value) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(key)) {
            return url;
        }
        Map<String, String> map = new HashMap<String, String>(1);
        map.put(key, value);
        return addParams(url, map);
    }

    /**
     * 给url中添加一堆键值对
     *
     * @param url
     * @param kvs
     *
     * @return
     */
    public static String addParams(String url, Map<String, String> kvs) {
        if (TextUtils.isEmpty(url) || null == kvs) {
            return url;
        }
        Uri origin = Uri.parse(url);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(origin.getScheme()).authority(origin.getAuthority()).path(origin.getPath())
                .query(origin.getQuery());
        Iterator<String> iterator = kvs.keySet().iterator();

        if (null != iterator) {
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = kvs.get(key);
                builder.appendQueryParameter(key, value);
            }
        }
        return builder.toString();
    }

    /**
     * 获取url的host.
     *
     * @param url url地址.
     *
     * @return host地址
     */
    public static String getUrlHost(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        Uri origin = Uri.parse(url);
        return origin.getHost();
    }

}
