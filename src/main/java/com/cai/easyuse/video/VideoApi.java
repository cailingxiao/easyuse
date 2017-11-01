package com.cai.easyuse.video;

import com.cai.easyuse.video.base.AbsVideoControl;
import com.cai.easyuse.video.base.AbsVideoView;
import com.cai.easyuse.video.imp.DefaultVideoControl;
import com.cai.easyuse.video.imp.DefaultVideoView;

import android.content.Context;

/**
 * 播放器api类
 * <p>
 * Created by cailingxiao on 2017/1/20.
 */
public final class VideoApi {
    private VideoApi() {

    }

    /**
     * 创建播放器view
     *
     * @param context
     *
     * @return
     */
    public static AbsVideoView createVideoView(Context context) {
        return new DefaultVideoView(context);
    }

    /**
     * 创建播放器控制器
     *
     * @param context
     *
     * @return
     */
    public static AbsVideoControl createVideoControl(Context context) {
        return new DefaultVideoControl(context);
    }

}
