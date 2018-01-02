/*
 * Copyright (C) 2017 cailingxiao, Inc. All Rights Reserved.
 */
package com.cai.easyuse.video.base;

import android.view.View;

/**
 * 播放器接口
 * <p>
 * Created by cailingxiao on 2017/1/20.
 */
public interface IVideo {
    void setPlaybackSpeed(float speed);

    void setLoop(boolean needLoop);

    void setVolume(float volume);

    float getPlaybackSpeed();

    void seekTo(long position);

    boolean needLoop();

    long getCurrentPosition();

    long getDuration();

    /**
     * 视频地址，可以以http，file开头
     *
     * @param fileUri
     */
    void play(String fileUri);

    void pause();

    void resume();

    void stop();

    void release();

    void setOnVideoListener(OnVideoListener listener);

    View getView();

    boolean isPaused();
}
