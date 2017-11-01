/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.cai.easyuse.statistics.base.StatisticsCycle;
import com.cai.easyuse.statistics.config.StatisticsConfig;
import com.cai.easyuse.statistics.imp.UmengImp;
import com.cai.easyuse.statistics.uc.UcCycle;
import com.cai.easyuse.statistics.uc.UcImp;

import android.app.Activity;
import android.content.Context;

/**
 * 数据统计包装实现类
 * <p>
 * Created by cailingxiao on 2017/1/10.
 */
class StatisticsProxy implements StatisticsCycle, UcCycle {
    private static volatile StatisticsProxy sInstance = null;

    /**
     * 存储统计实体
     */
    private Set<StatisticsCycle> mStat = new HashSet<StatisticsCycle>(2);

    /**
     * 账号统计相关
     */
    private UcCycle mUcCycle = null;

    /**
     * 是否已经初始化
     */
    private AtomicBoolean isInit = new AtomicBoolean(false);

    private StatisticsProxy() {
        // 增加内置的友盟统计
        mStat.add(new UmengImp());
    }

    public static StatisticsProxy getInstance() {
        if (null == sInstance) {
            synchronized(StatisticsProxy.class) {
                if (null == sInstance) {
                    sInstance = new StatisticsProxy();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context, StatisticsConfig config) {
        for (StatisticsCycle cycle : mStat) {
            cycle.init(context, config);
        }
        isInit.set(true);
    }

    @Override
    public void onExit(Context context) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onExit(context);
            }
        }
    }

    @Override
    public void onResume(Activity activity) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onResume(activity);
            }
        }
    }

    @Override
    public void onPause(Activity activity) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onPause(activity);
            }
        }
    }

    @Override
    public void onPageStart(Context context, String pageName) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onPageStart(context, pageName);
            }
        }
    }

    @Override
    public void onPageEnd(Context context, String pageName) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onPageEnd(context, pageName);
            }
        }
    }

    @Override
    public void onEvent(Context context, String actName, String actId, Map data) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onEvent(context, actName, actId, data);
            }
        }
    }

    @Override
    public void onEventStart(Context context, String actName, String actId, Map data) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onEventStart(context, actName, actId, data);
            }
        }
    }

    @Override
    public void onEventEnd(Context context, String actName, String actId, Map data) {
        if (isInit.get()) {
            for (StatisticsCycle cycle : mStat) {
                cycle.onEventEnd(context, actName, actId, data);
            }
        }
    }

    public void registerNew(StatisticsCycle selfMadeCycle) {
        mStat.add(selfMadeCycle);
    }

    @Override
    public void onProfileSignIn(String from, String id) {
        if (null == mUcCycle) {
            mUcCycle = new UcImp();
        }
        mUcCycle.onProfileSignIn(from, id);
    }

    @Override
    public void onProfileSignOff() {
        if (null == mUcCycle) {
            mUcCycle = new UcImp();
        }
        mUcCycle.onProfileSignOff();
    }
}
