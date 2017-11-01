/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.uc;

/**
 * 用户中心统计登录用户相关
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public interface UcCycle {
    void onProfileSignIn(String from, String id);

    void onProfileSignOff();
}
