package com.cai.easyuse.hybrid.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.cai.easyuse.config.EasyConfigApi;
import com.cai.easyuse.hybrid.annotation.H5CallNa;
import com.cai.easyuse.hybrid.constant.BuiWebHandlerConstant;
import com.cai.easyuse.util.LogUtils;

import android.content.Context;
import android.text.TextUtils;

/**
 * 获取H5要调用的方法并调用指定文件去执行
 * <p>
 * 指定文件在assets目录下，
 *
 * @author cailingxiao
 * @date 2016年3月3日
 */
public class BuiInvokeManager {

    private static final String TAG = "BuiInvokeManager";

    private static BuiInvokeManager sManager = null;
    private static final Object LOCKED = new Object();

    private Map<String, String> mMethodMap = null;
    private String mCompleteClassName;
    private Class mHandlerClass;
    private Object mHandlerInstance;

    /**
     * 初始化
     */
    private BuiInvokeManager() {
        String content = EasyConfigApi.getInstance().getProp(BuiWebHandlerConstant.DEFAULT_HANDLER_NODE);
        if (TextUtils.isEmpty(content)) {
            throw new RuntimeException(
                    "请先在assets根目录建立"
                            + EasyConfigApi.CONFIG_NAME
                            + "文件并完成相应配置,请参考com.cai.easyuse.component.webview.core.BuiDefaultInvokeHandler，如果不需要,"
                            + "请调用BuiWebView.setAllowH5Invoke(false)");
        }
        try {
            mCompleteClassName = content;
            mHandlerClass = Class.forName(mCompleteClassName);
            mHandlerInstance = mHandlerClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("没有找到类" + mCompleteClassName);
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 将调起名和真实的方法名关联起来
        mMethodMap = new HashMap<String, String>();
        Method[] methods = mHandlerClass.getDeclaredMethods();
        final int len = methods.length;
        H5CallNa annotation;
        String invokeName;
        for (int i = 0; i < len; i++) {
            annotation = methods[i].getAnnotation(H5CallNa.class);
            if (null != annotation) {
                invokeName = annotation.invokeName();
                // 调起名一样使用第一个声明
                if (!mMethodMap.containsKey(invokeName)) {
                    mMethodMap.put(invokeName, methods[i].getName());
                }
            }
        }
    }

    /**
     * 单例
     *
     * @return
     */
    public static BuiInvokeManager getInstance() {
        if (null == sManager) {
            synchronized (LOCKED) {
                if (null == sManager) {
                    sManager = new BuiInvokeManager();
                }
            }
        }
        return sManager;
    }

    /**
     * h5调用本地的方法，可能比较费时，建议在非ui线程中使用
     *
     * @param ctx
     * @param invokeName
     * @param args
     *
     * @return
     */
    public String callNativeFun(Context ctx, String invokeName, JSONObject args) {
        // 找到指定的方法，然后调用
        if (mMethodMap.containsKey(invokeName)) {
            final String methodName = mMethodMap.get(invokeName);
            try {
                Method method = mHandlerClass.getDeclaredMethod(methodName, Context.class, JSONObject.class);
                method.setAccessible(true);
                return method.invoke(mHandlerInstance, ctx, args) + "";
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        return "";
    }
}
