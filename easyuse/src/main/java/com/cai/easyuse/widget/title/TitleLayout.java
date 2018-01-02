package com.cai.easyuse.widget.title;

import com.cai.easyuse.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 简单通用菜单
 * <p>
 * Created by cailingxiao on 2016/10/3.
 */
public class TitleLayout extends RelativeLayout {
    private View mRoot;
    private ImageView mIvBack;
    private TextView mTvLeft;
    private TextView mTvCenter;
    private TextView mTvRight;
    private ProgressBar mPbLoading;

    public TitleLayout(Context context) {
        super(context);
        init(context, null);
    }

    public TitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        if (!isInEditMode()) {
            // 初始化变量
            mRoot = LayoutInflater.from(ctx).inflate(R.layout.title_layout, this, false);
            addView(mRoot);
            mIvBack = (ImageView) findViewById(R.id.title_back_img);
            mTvLeft = (TextView) findViewById(R.id.title_left_text);
            mTvCenter = (TextView) findViewById(R.id.title_center_text);
            mTvRight = (TextView) findViewById(R.id.title_right_text);
            mPbLoading = (ProgressBar) findViewById(R.id.title_loading);

            if (null != attrs) {
                TypedArray typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.TitleLayout);
                Drawable backSrc = typedArray.getDrawable(R.styleable.TitleLayout_btlBackSrc);
                if (null != backSrc) {
                    mIvBack.setImageDrawable(backSrc);
                }
                titleLeftTxt(typedArray.getString(R.styleable.TitleLayout_btlLeftText));
                titleCenterTxt(typedArray.getString(R.styleable.TitleLayout_btlCenterText));
                titleRightTxt(typedArray.getString(R.styleable.TitleLayout_btlRightText));
                int drawable = typedArray.getColor(R.styleable.TitleLayout_btlBackground, Color.WHITE);
                mRoot.setBackgroundColor(drawable);
                boolean backClose = typedArray.getBoolean(R.styleable.TitleLayout_btlBackClose, true);
                if (backClose) {
                    titleBackClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (getContext() instanceof Activity) {
                                ((Activity) getContext()).finish();
                            }
                        }
                    });
                }
                typedArray.recycle();
            }
        }
    }

    public View getRootView() {
        return mRoot;
    }

    public void showLoading() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mPbLoading.setVisibility(View.GONE);
    }

    public TitleLayout backImg(int resId) {
        mIvBack.setImageResource(resId);
        return this;
    }

    public TitleLayout background(int color) {
        mRoot.setBackgroundColor(color);
        return this;
    }

    public TitleLayout titleBackClickListener(OnClickListener backClickListener) {
        mIvBack.setOnClickListener(backClickListener);
        mIvBack.setVisibility(View.VISIBLE);
        return this;
    }

    public TitleLayout titleLeftTxt(CharSequence leftTxt) {
        mTvLeft.setText(leftTxt);
        if (TextUtils.isEmpty(leftTxt)) {
            mTvLeft.setVisibility(View.GONE);
        } else {
            mTvLeft.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public TitleLayout titleCenterTxt(CharSequence centerTxt) {
        mTvCenter.setText(centerTxt);
        if (TextUtils.isEmpty(centerTxt)) {
            mTvCenter.setVisibility(View.GONE);
        } else {
            mTvCenter.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public TitleLayout titleRightTxt(CharSequence rightTxt) {
        mTvRight.setText(rightTxt);
        if (TextUtils.isEmpty(rightTxt)) {
            mTvRight.setVisibility(View.GONE);
        } else {
            mTvRight.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public TitleLayout titleRightClickListener(OnClickListener clickListener) {
        mTvRight.setOnClickListener(clickListener);
        return this;
    }
}
