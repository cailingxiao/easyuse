package com.cai.easyuse.route;

import com.cai.easyuse.route.core.DefaultRoute;
import com.cai.easyuse.route.core.IRoute;

import android.content.Context;
import android.os.Bundle;

/**
 * 路由api
 * <p>
 * Created by cailingxiao on 2017/1/23.
 */
public final class RouteApi {
    private static volatile RouteApi sInstance = null;
    private IRoute mRoute = null;

    private RouteApi() {
        mRoute = new DefaultRoute();
    }

    public static RouteApi getInstance() {
        if (null == sInstance) {
            synchronized (RouteApi.class) {
                if (null == sInstance) {
                    sInstance = new RouteApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 路由到指定的activity的类
     *
     * @param context
     * @param targetClassName
     * @param data
     * @param requestCode
     */
    public boolean route(Context context, String targetClassName, Bundle data, int requestCode) {
        String routeStr = mRoute.formRouteStr(targetClassName, requestCode);
        return mRoute.routeTo(context, routeStr, data);
    }

    public boolean route(Context context, String targetClassName, int requestCode) {
        return route(context, targetClassName, null, requestCode);
    }

    public boolean route(Context context, Class targetClass, int requestCode) {
        return route(context, targetClass.getName(), null, requestCode);
    }

    public boolean route(Context context, Class targetClass, Bundle data, int requestCode) {
        return route(context, targetClass.getName(), data, requestCode);
    }

    public boolean route(Context context, String targetClassName, Bundle data) {
        return route(context, targetClassName, data, -1);
    }

    public boolean route(Context context, String targetClassName) {
        return route(context, targetClassName, null, -1);
    }

    public boolean route(Context context, Class clazz) {
        return route(context, clazz.getName());
    }

    public boolean route(Context context, Class clazz, Bundle data) {
        return route(context, clazz.getName(), data);
    }

}
