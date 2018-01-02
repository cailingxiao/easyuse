package com.cai.easyuse.route.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cai.easyuse.util.ContextUtils;
import com.cai.easyuse.util.ConvertUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 默认路由跳转
 * <p>
 * Created by cailingxiao on 2017/1/23.
 */
public class DefaultRoute implements IRoute {
    private static final String ROUTE_REG = "(\\w+)://([\\w|\\.]+)(\\?\\w+=(\\d+))?";
    private static final String SCHEMA_HEAD = "arjp";
    private static final String REQUEST_CODE = "requestCode";
    private Pattern mPattern = Pattern.compile(ROUTE_REG);

    public DefaultRoute() {

    }

    @Override
    public boolean routeTo(Context context, String routeStr, Bundle data) {
        if (verify(context, routeStr)) {
            return parseRoute(context, routeStr, data);
        }
        return false;
    }

    @Override
    public String formRouteStr(String className, int requestCode) {
        StringBuffer sb = new StringBuffer();
        sb.append(SCHEMA_HEAD);
        sb.append("://");
        sb.append(className);
        if (-1 != requestCode) {
            sb.append("?");
            sb.append(REQUEST_CODE);
            sb.append("=");
            sb.append(requestCode);
        }
        return sb.toString();
    }

    // arjp://targetclassname?requestCode=x
    // \w+://(\w+\.+)+(\?*)
    private boolean parseRoute(Context context, String routeStr, Bundle data) {
        if (Pattern.matches(ROUTE_REG, routeStr)) {
            Intent intent = new Intent();
            Matcher matcher = mPattern.matcher(routeStr);
            matcher.matches();
            String schema = matcher.group(1);
            String target = matcher.group(2);
            String params = matcher.group(4);
            if (SCHEMA_HEAD.equals(schema)) {
                intent.setClassName(context, target);
                if (null != data) {
                    intent.putExtras(data);
                }
                return launch(context, intent, params);
            }
        }
        return false;
    }

    /**
     * 启动activity
     *
     * @param context
     * @param intent
     * @param params
     *
     * @return
     */
    private boolean launch(Context context, Intent intent, String params) {
        if (ContextUtils.isIntentAvailable(intent)) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                // activity 模式需要带上requestCode
                final int reqCode = ConvertUtils.str2Int(params, -1);
                activity.startActivityForResult(intent, reqCode);
            } else {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            return true;
        }
        return false;
    }

    private boolean verify(Context context, String routeStr) {
        return null != context && !TextUtils.isEmpty(routeStr);
    }
}
