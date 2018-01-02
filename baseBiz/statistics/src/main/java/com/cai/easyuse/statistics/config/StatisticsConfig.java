/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.statistics.config;

/**
 * 统计服务配置相关，现在里面只有渠道相关，后续如果初始化还有其他参数，可以直接在这里添加
 * <p>
 * Created by cailingxiao on 2017/1/11.
 */
public class StatisticsConfig {
    private String mChannel = null;
    private String mUid = null;
    private String mUmengAppkey = null;

    public String getChannel() {
        return mChannel;
    }

    public String getUid() {
        return mUid;
    }

    public String getUmengAppkey() {
        return mUmengAppkey;
    }

    public static class Builder {
        private String mChannel = "no-channel";
        private String mUid = "";
        private String mUmengAppkey = null;

        public Builder channel(String channel) {
            mChannel = channel;
            return this;
        }

        public Builder uid(String uid) {
            mUid = uid;
            return this;
        }

        public Builder umengAppkey(String appKey) {
            mUmengAppkey = appKey;
            return this;
        }

        public StatisticsConfig create() {
            StatisticsConfig config = new StatisticsConfig();
            config.mChannel = mChannel;
            config.mUid = mUid;
            config.mUmengAppkey = mUmengAppkey;
            return config;
        }
    }
}
