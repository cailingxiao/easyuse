package com.cai.easyuse.video.base;

import android.view.View;

/**
 * Created by cailingxiao on 2017/3/13.
 */

public interface IVideoControl {
    void setVideo(IVideo video);

    void setBackClickListener(View.OnClickListener backClickListener);

    void setTitle(CharSequence title);

    void setSpeed(float speed);

}
