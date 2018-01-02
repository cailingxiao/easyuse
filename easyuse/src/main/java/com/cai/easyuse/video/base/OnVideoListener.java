package com.cai.easyuse.video.base;

/**
 * 播放器回调
 * <p>
 * Created by cailingxiao on 2017/1/20.
 */
public interface OnVideoListener {
    void onStart();

    void onPlayOver();

    void onPlayError();
}
