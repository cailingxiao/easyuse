/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics;

import android.app.Activity;
import android.content.Context;

import com.cai.easyuse.statistics.base.StatisticsCycle;
import com.cai.easyuse.statistics.config.StatisticsConfig;
import com.cai.easyuse.statistics.util.StatisticsUtils;

import java.util.Map;

/**
 * 数据统计模块统一入口，推荐直接使用这个接口进行统计
 * <p>
 * Created by cailingxiao on 2017/1/10.
 */
public class StatisticsDataApi {
    private StatisticsDataApi() {
    }

    /**
     * 在初始化的时候调用，注意对多进程进行区分，只在ui进程进行初始化
     *
     * @param appContext
     * @param channel
     */
//    public static synchronized void init(Context appContext, String channel) {
//        String ch = "no-channel";
//        if (!TextUtils.isEmpty(channel)) {
//            ch = channel;
//        }
//        StatisticsConfig.Builder builder = new StatisticsConfig.Builder();
//        init(appContext, builder.channel(ch).create());
//    }

    /**
     * 在初始化的时候调用，注意对多进程进行区分，只在ui进程进行初始化
     *
     * @param appContext
     */
    public static synchronized void init(Context appContext, StatisticsConfig config) {
        StatisticsProxy.getInstance().init(appContext, config);
    }

    /**
     * 在应用退出前调用
     *
     * @param context
     */
    public static synchronized void onExit(Context context) {
        StatisticsProxy.getInstance().onExit(context);
    }

    /**
     * 增加用户自己的统计实现，只能在init前调用
     *
     * @param selfMadeCycle
     */
    public static synchronized void registerNew(StatisticsCycle selfMadeCycle) {
        StatisticsProxy.getInstance().registerNew(selfMadeCycle);
    }

    /**
     * 在activity的onResume函数中调用
     *
     * @param activity
     */
    public static synchronized void onResume(Activity activity) {
        StatisticsProxy.getInstance().onResume(activity);
    }

    /**
     * 在activity的onPause函数中调用
     *
     * @param activity
     */
    public static synchronized void onPause(Activity activity) {
        StatisticsProxy.getInstance().onPause(activity);
    }

    /**
     * 在fragment的onResume中调用，也可以在某些tabHost中调用，对视图级别进行统计
     *
     * @param context
     * @param pageName
     */
    public static synchronized void onPageStart(Context context, String pageName) {
        StatisticsProxy.getInstance().onPageStart(context, pageName);
    }

    /**
     * 在fragment的onPause中调用,也可以在某些tabHost中调用，对视图级别进行统计
     *
     * @param context
     * @param pageName
     */
    public static synchronized void onPageEnd(Context context, String pageName) {
        StatisticsProxy.getInstance().onPageEnd(context, pageName);
    }

    /**
     * 打点
     *
     * @param context 当前上下文
     * @param actName 统计别名，不作为key使用的
     * @param actId   统计点，作为key使用
     * @param data    附加数据
     */
    public static synchronized void onEvent(Context context, String actName, String actId, Map data) {
        StatisticsProxy.getInstance().onEvent(context, actName, actId, data);
    }

    /**
     * 打点
     *
     * @param context 当前上下文
     * @param actName 统计别名
     * @param actId   统计点
     * @param data    附加数据，使用Object数组形式，奇数个会丢弃最后一个值，将剩下的形成map
     */
    public static synchronized void onEvent(Context context, String actName, String actId,
                                            Object... data) {
        onEvent(context, actName, actId, StatisticsUtils.array2Map(data));
    }

    /**
     * 没有附加数据的打点
     *
     * @param context
     * @param actName
     * @param actId
     */
    public static synchronized void onEvent(Context context, String actName, String actId) {
        onEvent(context, actName, actId, null, null);
    }

    /**
     * 没有别名的打点
     *
     * @param context
     * @param actId
     */
    public static synchronized void onEvent(Context context, String actId) {
        onEvent(context, null, actId, null, null);
    }

    /**
     * 打计算点-start
     *
     * @param context
     * @param actName
     * @param actId
     * @param data
     */
    public static synchronized void onEventStart(Context context, String actName, String actId,
                                                 Map data) {
        StatisticsProxy.getInstance().onEventStart(context, actName, actId, data);
    }

    /**
     * 打计算点-end
     *
     * @param context
     * @param actName
     * @param actId
     * @param data
     */
    public static synchronized void onEventEnd(Context context, String actName, String actId,
                                               Map data) {
        StatisticsProxy.getInstance().onEventEnd(context, actName, actId, data);
    }

    /**
     * 用户登录统计账号相关
     *
     * @param accountFrom
     * @param uid
     */
    public static synchronized void setUserSignin(String accountFrom, String uid) {
        StatisticsProxy.getInstance().onProfileSignIn(accountFrom, uid);
    }

    /**
     * 用户登出统计账号相关
     */
    public static synchronized void setUserSignout() {
        StatisticsProxy.getInstance().onProfileSignOff();
    }

}
