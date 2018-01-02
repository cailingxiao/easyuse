package com.cai.easyuse.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义view
 * <p>
 * Created by cailingxiao on 2017/2/11.
 */
public abstract class AbsCustomView extends View {
    public AbsCustomView(Context context) {
        super(context);
        init(null);
    }

    public AbsCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AbsCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthLen = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (MeasureSpec.AT_MOST == widthMode) {
            // wrap_content
            widthLen = getDefaultWidth();
        }
        int heightLen = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (MeasureSpec.AT_MOST == heightMode) {
            // wrap_content
            heightLen = getDefaultHeight();
        }
        setMeasuredDimension(widthLen, heightLen);
    }

    /**
     * 初始化页面
     *
     * @param attrs
     */
    protected abstract void initView(AttributeSet attrs);

    /**
     * 获取默认的长度
     *
     * @return
     */
    protected abstract int getDefaultWidth();

    /**
     * 获取默认的高度
     *
     * @return
     */
    protected abstract int getDefaultHeight();
}
