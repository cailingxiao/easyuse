package com.cai.easyuse.video.imp;

import java.util.HashSet;
import java.util.Set;

import com.cai.easyuse.util.LogUtils;
import com.cai.easyuse.video.base.AbsVideoView;
import com.cai.easyuse.video.base.OnVideoListener;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.VideoView;

/**
 * 默认实现，可作为view使用
 * <p>
 * Created by cailingxiao on 2017/1/20.
 */
public class DefaultVideoView extends AbsVideoView {
    private VideoView mVideo;
    private MediaPlayer mMediaPlayer;
    private Set<OnVideoListener> mListeners = new HashSet<OnVideoListener>();

    /**
     * 播放器设置播放url后，是否已经prepared
     */
    private boolean isPrepared = false;

    /**
     * 是否需要重复播放
     */
    private boolean needLoop = false;

    /**
     * 当前播放的uri
     */
    private String mCurrentUri = null;

    public DefaultVideoView(Context context) {
        super(context);
        initView();
    }

    public DefaultVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DefaultVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mVideo = new VideoView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        addView(mVideo, lp);
        // 播放过程中是否出错
        mVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                isPrepared = false;
                if (null != mListeners) {
                    synchronized (DefaultVideoView.class) {
                        for (OnVideoListener listener : mListeners) {
                            listener.onPlayError();
                        }
                    }
                }
                return false;
            }
        });

        // 是否播放完成
        mVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPrepared = false;
                if (null != mListeners) {
                    synchronized (DefaultVideoView.class) {
                        for (OnVideoListener listener : mListeners) {
                            listener.onPlayOver();
                        }
                    }
                }
                if (needLoop && !TextUtils.isEmpty(mCurrentUri)) {
                    play(mCurrentUri);
                }
            }
        });

        // 是否已经开始播放
        mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                mMediaPlayer = mp;
                if (null != mListeners) {
                    synchronized (DefaultVideoView.class) {
                        for (OnVideoListener listener : mListeners) {
                            listener.onStart();
                        }
                    }
                }

            }
        });

        init(getContext());
    }

    private void init(Context context) {

    }

    @Override
    public void setLoop(boolean needLoop) {
        //        if (!isPrepared) {
        //            return;
        //        }
        //        mVideo.setLoop(needLoop);
        this.needLoop = needLoop;
    }

    @Override
    public void setVolume(float volume) {
        if (null != mMediaPlayer) {
            mMediaPlayer.setVolume(volume, volume);
        }
    }

    @Override
    public float getPlaybackSpeed() {
        if (!isPrepared) {
            return 1;
        }
        return 1;
    }

    @Override
    public synchronized void setPlaybackSpeed(float speed) {
//        if (!isPrepared) {
//            return;
//        }
//        if (Math.abs(mVideo.getPlaybackSpeed() - speed) > 1e-5) {
//            // gapToChange(speed);
//            mVideo.setPlaybackSpeed(speed);
//        }
    }

    @Override
    public void seekTo(long position) {
        if (!isPrepared) {
            return;
        }
        mVideo.seekTo((int)position);
    }

    @Override
    public boolean needLoop() {
        //        if (!isPrepared) {
        //            return false;
        //        }
        //        return mVideo.isLooping();
        return needLoop;
    }

    @Override
    public long getCurrentPosition() {
        if (!isPrepared) {
            return 0;
        }
        return mVideo.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        if (!isPrepared) {
            return 0;
        }
        return mVideo.getDuration();
    }

    @Override
    public void play(String fileUri) {
        isPrepared = false;
        mCurrentUri = fileUri;
        mVideo.setVideoPath(fileUri);
        mVideo.start();
    }

    @Override
    public void pause() {
        if (!isPrepared) {
            return;
        }
        mVideo.pause();
    }

    @Override
    public void resume() {
        if (!isPrepared) {
            return;
        }
        mVideo.start();
    }

    @Override
    public void stop() {
        if (!isPrepared) {
            return;
        }
        mVideo.stopPlayback();
    }

    @Override
    public void release() {
        mVideo.stopPlayback();
        mListeners.clear();
    }

    @Override
    public synchronized void setOnVideoListener(OnVideoListener listener) {
        mListeners.add(listener);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isPaused() {
        if (!isPrepared) {
            return true;
        }
        return !mVideo.isPlaying();
    }

    private synchronized void gapToChange(float speed) {
        float nowSpeed = getPlaybackSpeed();
        float gap = Math.abs(nowSpeed - speed);
        int times = (int) (gap / 0.1f);
        final boolean inc = speed - nowSpeed > 0;
        for (int i = 0; i <= times; i++) {
            if (inc) {
                nowSpeed += 0.1f;
            } else {
                nowSpeed -= 0.1f;
            }
            LogUtils.e("xxx", "nowSpeed=" + nowSpeed);

//            mVideo.setPlaybackSpeed(nowSpeed);
        }
    }

}
