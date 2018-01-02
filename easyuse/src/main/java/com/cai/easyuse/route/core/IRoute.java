package com.cai.easyuse.route.core;

import android.content.Context;
import android.os.Bundle;

/**
 * 路由接口
 * <p>
 * routeStr 包含跳转的地址等.默认指定跳转协议(Android Route Jump Protocol)如下：
 * </p>
 * <p>
 * arjp://targetclassname?requestCode=x
 * </p>
 * <p>
 * requestCode可选
 * </p>
 * Created by cailingxiao on 2017/1/23.
 */
public interface IRoute {

    /**
     * 路由目的地
     *
     * @param context  跳转使用的context
     * @param routeStr 跳转的路由地址
     * @param data     携带的数据，可以为空
     */
    boolean routeTo(Context context, String routeStr, Bundle data);

    /**
     *
     * @param className
     * @return
     */
    String formRouteStr(String className, int requestCode);
}
