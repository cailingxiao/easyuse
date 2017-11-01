/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.imp;

import java.util.Map;

import com.cai.easyuse.statistics.base.StatisticsCycle;
import com.cai.easyuse.statistics.config.StatisticsConfig;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;

/**
 * 内置友盟统计
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public class UmengImp implements StatisticsCycle {
    @Override
    public void init(Context context, StatisticsConfig config) {
        MobclickAgent.UMAnalyticsConfig conf = new MobclickAgent.UMAnalyticsConfig(context, config.getUmengAppkey(),
                config.getChannel(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.startWithConfigure(conf);
    }

    @Override
    public void onExit(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    @Override
    public void onResume(Activity activity) {
        MobclickAgent.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    @Override
    public void onPageStart(Context context, String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    @Override
    public void onPageEnd(Context context, String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    @Override
    public void onEvent(Context context, String actName, String actId, Map data) {
        MobclickAgent.onEvent(context, actId, data);
    }

    @Override
    public void onEventStart(Context context, String actName, String actId, Map data) {
        // 不支持
    }

    @Override
    public void onEventEnd(Context context, String actName, String actId, Map data) {
        // 不支持
    }
}
