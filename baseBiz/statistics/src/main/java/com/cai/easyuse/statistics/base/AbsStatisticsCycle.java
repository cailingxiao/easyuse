/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.base;

import android.content.Context;

import java.util.Map;


/**
 * 抽象统计实现类，已经实现部分
 * <p>
 * Created by cailingxiao on 2017/1/11.
 */
public abstract class AbsStatisticsCycle implements StatisticsCycle {
    @Override
    public void onExit(Context context) {

    }

    @Override
    public void onPageStart(Context context, String pageName) {

    }

    @Override
    public void onPageEnd(Context context, String pageName) {

    }

    @Override
    public void onEventStart(Context context, String actName, String actId, Map data) {

    }

    @Override
    public void onEventEnd(Context context, String actName, String actId, Map data) {

    }
}
