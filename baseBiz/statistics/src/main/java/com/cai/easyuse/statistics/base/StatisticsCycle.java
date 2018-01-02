package com.cai.easyuse.statistics.base;

import android.app.Activity;
import android.content.Context;

import com.cai.easyuse.statistics.config.StatisticsConfig;

import java.util.Map;

/**
 * 统计完全的周期函数
 * <p>
 * Created by cailingxiao on 2017/1/10.
 */
public interface StatisticsCycle {
    /**
     * 初始化的时候调用，会在主线程中进行，不要在这里做耗时操作
     *
     * @param context
     * @param config
     */
    void init(Context context, StatisticsConfig config);

    /**
     * 应用exit之前调用
     *
     * @param context
     */
    void onExit(Context context);

    /**
     * 应用(恢复)启动调用
     *
     * @param activity
     */
    void onResume(Activity activity);

    /**
     * 应用进入不可见调用
     *
     * @param activity
     */
    void onPause(Activity activity);

    /**
     * 在activity内部实现更加细节的页面统计
     *
     * @param pageName
     */
    void onPageStart(Context context, String pageName);

    /**
     * 在activity内部实现更加细节的页面统计
     *
     * @param pageName
     */
    void onPageEnd(Context context, String pageName);

    /**
     * 增加统计打点事件
     *
     * @param context
     * @param actName 事件名，注释作用，也可上传，取决于实现类策略
     * @param actId   事件id，上传后台唯一的字段
     * @param data    附加数据，可能为空
     */
    void onEvent(Context context, String actName, String actId, Map data);

    /**
     * 为页面增加计算统计-start
     *
     * @param context
     * @param actName
     * @param actId
     * @param data
     */
    void onEventStart(Context context, String actName, String actId, Map data);

    /**
     * 为页面增加计算统计-end
     *
     * @param context
     * @param actName
     * @param actId
     * @param data
     */
    void onEventEnd(Context context, String actName, String actId, Map data);
}
