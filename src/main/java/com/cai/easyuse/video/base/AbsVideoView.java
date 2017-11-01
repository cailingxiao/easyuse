package com.cai.easyuse.video.base;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 视频view
 * <p>
 * Created by cailingxiao on 2017/1/20.
 */
public abstract class AbsVideoView extends FrameLayout implements IVideo {

    public AbsVideoView(Context context) {
        super(context);
    }

    public AbsVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
