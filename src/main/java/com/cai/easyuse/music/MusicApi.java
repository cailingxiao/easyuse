package com.cai.easyuse.music;

import java.io.File;

import com.cai.easyuse.event.EventApi;
import com.cai.easyuse.event.EventConstant;
import com.cai.easyuse.music.core.MusicFlagConstant;
import com.cai.easyuse.music.core.MusicPlayerService;
import com.cai.easyuse.util.ContextUtils;

import android.content.Intent;
import android.text.TextUtils;

/**
 * 音乐播放器接口
 * <p>
 * Created by cailingxiao on 2017/1/15.
 */
public final class MusicApi implements EventApi.IBuiEvent {
    private static volatile MusicApi sInstance = null;
    private PlayingListener mListener = null;

    private MusicApi() {
        EventApi.registerEvent(this);
    }

    public static MusicApi getInstance() {
        if (null == sInstance) {
            synchronized(MusicApi.class) {
                if (null == sInstance) {
                    sInstance = new MusicApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 播放音乐
     *
     * @param musicFile
     */
    public void play(File musicFile, boolean needLoop) {
        if (null != musicFile && musicFile.exists()) {
            Intent intent = new Intent();
            intent.setClass(ContextUtils.getContext(), MusicPlayerService.class);
            intent.putExtra(MusicFlagConstant.PARAMS_FILE_URL, musicFile.getAbsolutePath());
            intent.putExtra(MusicFlagConstant.PARAMS_MSG_FLAG, MusicFlagConstant.MSG_PLAY);
            intent.putExtra(MusicFlagConstant.PARAMS_LOOP_PLAY, needLoop);
            ContextUtils.getContext().startService(intent);
        }
    }

    /**
     * 播放音乐
     *
     * @param filePath
     */
    public void play(String filePath, boolean needLoop) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            play(file, needLoop);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        Intent intent = new Intent();
        intent.setClass(ContextUtils.getContext(), MusicPlayerService.class);
        intent.putExtra(MusicFlagConstant.PARAMS_MSG_FLAG, MusicFlagConstant.MSG_PAUSE);
        ContextUtils.getContext().startService(intent);
    }

    /**
     * 继续播放
     */
    public void resume() {
        Intent intent = new Intent();
        intent.setClass(ContextUtils.getContext(), MusicPlayerService.class);
        intent.putExtra(MusicFlagConstant.PARAMS_MSG_FLAG, MusicFlagConstant.MSG_RESUME);
        ContextUtils.getContext().startService(intent);
    }

    /**
     * 停止播放
     */
    public void stop() {
        Intent intent = new Intent();
        intent.setClass(ContextUtils.getContext(), MusicPlayerService.class);
        intent.putExtra(MusicFlagConstant.PARAMS_MSG_FLAG, MusicFlagConstant.MSG_STOP);
        ContextUtils.getContext().startService(intent);
    }

    /**
     * 注册音乐播放监听器
     */
    public void registerPlayingListener(PlayingListener listener) {
        mListener = listener;
    }

    @Override
    public void onEvent(EventApi.BuiEvent event) {
        if (null != event) {
            final int eventId = event.eventId;
            switch (eventId) {
                case EventConstant.EVENT_ID_MUSIC_START:
                    if (null != mListener) {
                        mListener.onMusicPlay(event.eventObj + "");
                    }
                    break;
                case EventConstant.EVENT_ID_MUSIC_END:
                    if (null != mListener) {
                        mListener.onMusicStop(event.eventObj + "");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 音乐播放监听器
     */
    public interface PlayingListener {
        void onMusicPlay(String path);

        void onMusicStop(String path);
    }

}
