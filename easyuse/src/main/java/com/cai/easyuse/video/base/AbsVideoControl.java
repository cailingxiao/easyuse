package com.cai.easyuse.video.base;

import com.cai.easyuse.base.AbsCustomViewGroup;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cailingxiao on 2017/3/13.
 */
public abstract class AbsVideoControl extends AbsCustomViewGroup implements IVideoControl {
    protected IVideo mVideo;
    protected OnClickListener mBackClickListener;

    public AbsVideoControl(Context context) {
        super(context);
    }

    public AbsVideoControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsVideoControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsVideoControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setVideo(IVideo video) {
        mVideo = video;
    }

    @Override
    public void setBackClickListener(OnClickListener backClickListener) {
        mBackClickListener = backClickListener;
    }

}
