package com.cai.easyuse.event;

/**
 * easyuse使用的通信,从1~1024
 * <p>
 * Created by cailingxiao on 2017/1/15.
 */
public final class EventConstant {
    /**
     * 音乐播放开始
     */
    public static final int EVENT_ID_MUSIC_START = 0;
    /**
     * 音乐播放结束
     */
    public static final int EVENT_ID_MUSIC_END = 1;
    /**
     * 应用语言切换-to中文
     */
    public static final int EVENT_ID_CHANGE_LANGUAGE_CN = 2;

    /**
     * 应用语言切换-to英文
     */
    public static final int EVENT_ID_CHANGE_LANGUAGE_EN = 3;

    /**
     * 关闭其他应用，只留下指定的应用，发送的数据有留下应用的全名
     */
    public static final int EVENT_ID_FINISH_OTHER = 4;
}
