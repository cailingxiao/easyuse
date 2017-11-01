package com.cai.easyuse.video.imp;

import com.cai.easyuse.hybrid.view.BuiWebView;
import com.cai.easyuse.video.base.AbsVideoView;
import com.cai.easyuse.video.base.OnVideoListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * h5实现的播放器，还没写完
 * <p>
 * Created by cailingxiao on 2017/3/11.
 */
public class H5VideoView extends AbsVideoView {
    private WebView mWebView;

    public H5VideoView(Context context) {
        super(context);
        init();
    }

    public H5VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public H5VideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) {
            return;
        }
        mWebView = new BuiWebView(getContext());
    }

    @Override
    public void setPlaybackSpeed(float speed) {

    }

    @Override
    public void setLoop(boolean needLoop) {

    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public float getPlaybackSpeed() {
        return 0;
    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public boolean needLoop() {
        return false;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public void play(String fileUri) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void release() {

    }

    @Override
    public void setOnVideoListener(OnVideoListener listener) {

    }

    @Override
    public View getView() {
        return null;
    }

    @Override
    public boolean isPaused() {
        return false;
    }
}
