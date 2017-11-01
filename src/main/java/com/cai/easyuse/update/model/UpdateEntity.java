package com.cai.easyuse.update.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.cai.easyuse.base.mark.BuiEntity;

/**
 * 更新实体类
 * <p>
 * Created by cailingxiao on 2017/3/19.
 */
public class UpdateEntity implements BuiEntity {
    /**
     * 应用名
     */
    @JSONField(name = "appVersion")
    public String appVersion;
    /**
     * 应用下载路径
     */
    @JSONField(name = "appPath")
    public String appPath;
    /**
     * 应用是否强制更新，0表示不强制，1表示强制
     */
    @JSONField(name = "appForceUpdate")
    public int appForceUpdate;
    /**
     * 更新说明
     */
    @JSONField(name = "appUpdateInfo")
    public String appUpdateInfo;
    /**
     * 更新时间
     */
    @JSONField(name = "appUpdateTime")
    public String appUpdateTime;
}
