package com.cai.easyuse.base;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 自定义viewgroup
 * <p>
 * Created by cailingxiao on 2017/2/11.
 */
public abstract class AbsCustomViewGroup extends FrameLayout {
    private View mContainer = null;

    public AbsCustomViewGroup(Context context) {
        super(context);
        init(null);
    }

    public AbsCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AbsCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        int layoutId = getLayoutId();
        if (-1 != layoutId) {
            mContainer = LayoutInflater.from(getContext()).inflate(layoutId, this, true);
        }

        mContainer = initView(mContainer);
        post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    public <T extends View> T findUi(int viewId) {
        return (T) mContainer.findViewById(viewId);
    }

    /**
     * 自定义layout的布局
     *
     * @return -1表示使用代码添加布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     *
     * @param root
     */
    protected abstract View initView(View root);


    /**
     * 初始化数据
     */
    protected void initData() {

    }

}
