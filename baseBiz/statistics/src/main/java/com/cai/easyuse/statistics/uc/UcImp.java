/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.uc;

import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;

/**
 * uc实现
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public class UcImp implements UcCycle {
    private static final String USER_ACCOUNT = "DEFAULT";

    @Override
    public void onProfileSignIn(String from, String id) {
        String realFrom = USER_ACCOUNT;
        if (!TextUtils.isEmpty(from)) {
            realFrom = from;
        }
        MobclickAgent.onProfileSignIn(realFrom, id);
    }

    @Override
    public void onProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }
}
