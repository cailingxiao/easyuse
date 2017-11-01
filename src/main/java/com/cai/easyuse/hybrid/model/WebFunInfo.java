package com.cai.easyuse.hybrid.model;

import org.json.JSONObject;

/**
 * 网页传递过来的msg解析成实体
 * <p>
 * Created by cailingxiao on 2017/8/16.
 */
public class WebFunInfo {
    /**
     * web端可被调用的方法名
     */
    public String webCallbackFunc;

    /**
     * web端标示请求的请求序号
     */
    public String webCallbackActionId;

    /**
     * na端被调用的方法
     */
    public String naCallMethod;

    /**
     * na端被调用时需要携带的参数
     */
    public JSONObject naCallArgs;

    @Override
    public String toString() {
        return "WebFunInfo{" +
                "webCallbackFunc='" + webCallbackFunc + '\'' +
                ", webCallbackActionId='" + webCallbackActionId + '\'' +
                ", naCallMethod='" + naCallMethod + '\'' +
                ", naCallArgs=" + naCallArgs +
                '}';
    }
}
