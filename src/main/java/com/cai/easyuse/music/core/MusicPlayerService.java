package com.cai.easyuse.music.core;

import com.cai.easyuse.event.EventApi;
import com.cai.easyuse.event.EventConstant;
import com.cai.easyuse.util.LogUtils;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * 音乐播放器实现类
 * <p>
 * Created by cailingxiao on 2017/1/15.
 */
public class MusicPlayerService extends Service {
    private static final String TAG = "MusicPlayerService";
    private MediaPlayer mediaPlayer = new MediaPlayer();       // 媒体播放器对象
    private String path;                        // 音乐文件路径
    private boolean isPause = false;
    private int mPos = 0;
    private boolean mLoop = false;  // 是否循环播放

    /**
     * 播放器播放完的监听
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            sendMusicOverEvent();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            dealAction(intent);
        }
        return Service.START_NOT_STICKY;
    }

    /**
     * 处理
     *
     * @param intent
     */
    private void dealAction(Intent intent) {
        String tmp = intent.getStringExtra(MusicFlagConstant.PARAMS_FILE_URL);
        if (!TextUtils.isEmpty(tmp)) {
            path = tmp;
        }
        if (intent.hasExtra(MusicFlagConstant.PARAMS_LOOP_PLAY)) {
            mLoop = intent.getBooleanExtra(MusicFlagConstant.PARAMS_LOOP_PLAY, false);
        }
        final int msg = intent.getIntExtra(MusicFlagConstant.PARAMS_MSG_FLAG, MusicFlagConstant.MSG_PLAY);
        switch (msg) {
            case MusicFlagConstant.MSG_PLAY:
                mPos = 0;
                play(0);
                break;
            case MusicFlagConstant.MSG_PAUSE:
                if (!isPause) {
                    pause();
                }
                break;
            case MusicFlagConstant.MSG_STOP:
                stop();
                break;
            case MusicFlagConstant.MSG_RESUME:
                if (isPause) {
                    play(mPos);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 播放音乐
     *
     * @param position
     */
    private void play(int position) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        try {
            mediaPlayer.reset(); // 把各项参数恢复到初始状态
            mediaPlayer.setLooping(mLoop);
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnCompletionListener(mCompletionListener);           // 完成时监听
            mediaPlayer.setOnPreparedListener(new PreparedListener(position));  // 注册一个监听器
            mediaPlayer.prepare();  // 进行缓冲
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停音乐
     */
    private void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            isPause = true;
            mPos = mediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 停止音乐
     */
    private void stop() {
        if (mediaPlayer != null) {
            if (mLoop) {
                mediaPlayer.setLooping(false);
                mediaPlayer.pause();
            }
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        mPos = 0;
        stopSelf();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mPos = 0;
    }

    /**
     * 发送音乐播放开始事件
     */
    private void sendMusicStartEvent() {
        EventApi.sendEvent(new EventApi.BuiEvent(EventConstant.EVENT_ID_MUSIC_START, path));
    }

    /**
     * 发送音乐播放结束事件
     */
    private void sendMusicOverEvent() {
        EventApi.sendEvent(new EventApi.BuiEvent(EventConstant.EVENT_ID_MUSIC_END, path));
    }

    /**
     * 实现一个OnPrepareLister接口,当音乐准备好的时候开始播放
     */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int positon;

        public PreparedListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();    // 开始播放
            if (positon > 0) {    // 如果音乐不是从头播放
                mediaPlayer.seekTo(positon);
            }
            isPause = false;
            sendMusicStartEvent();
        }
    }
}
