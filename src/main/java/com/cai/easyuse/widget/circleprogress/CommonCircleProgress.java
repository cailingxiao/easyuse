package com.cai.easyuse.widget.circleprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 通用圆形进度条
 * <p>
 * Created by lingxiaocai on 2017/6/13.
 */

public class CommonCircleProgress extends View {
    private static final String TAG = "CommonCircleProgress";
    private static final int CIRCLE_ANGEL = 360;

    private static final int DEFAULT_PROGRESS_WIDTH = 10;
    private static final int DEFAULT_BORDER_BG_WIDTH = 10;
    private static final int DEFAULT_WIDGET_SIZE = 100;

    /**
     * 是否顺时针
     */
    private boolean isClockWise = false;

    /**
     * 圆环背景
     */
    private int mBorderBgColor = Color.parseColor("#4c161a23");

    /**
     * 圆环背景宽度，默认和圆环进度宽度一致
     */
    private int mBorderBgWidth = DEFAULT_BORDER_BG_WIDTH;

    /**
     * 进度条颜色
     */
    private int mProgressColor = Color.parseColor("#4ca6ff");

    /**
     * 进度条宽度
     */
    private int mProgressWidth = DEFAULT_BORDER_BG_WIDTH;

    /**
     * 是否动画
     */
    private boolean isAnimated = true;

    /**
     * 动画时间
     */
    private int mAnimateTime = 500;

    /**
     * 最大值
     */
    private int mMaxProgress = 100;

    /**
     * 当前进度
     */
    private int mCurProgress = 0;

    /**
     * 当前扫过的区域
     */
    private int mCurSweepAngle = 0;

    /**
     * 起始点角度
     */
    private int mStartAngle = 270;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint();

    /**
     * 绘制区域
     */
    private RectF mDrawRect = new RectF();

    public CommonCircleProgress(Context context) {
        super(context);
        init(null);
    }

    public CommonCircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CommonCircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void setClockWise(boolean clockWise) {
        isClockWise = clockWise;
    }

    public void setBorderBgColor(int mBorderBgColor) {
        this.mBorderBgColor = mBorderBgColor;
    }

    public void setBorderBgWidth(int mBorderBgWidth) {
        this.mBorderBgWidth = mBorderBgWidth;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
    }

    public void setProgressWidth(int mProgressWidth) {
        this.mProgressWidth = mProgressWidth;
    }

    public void setAnimated(boolean animated) {
        isAnimated = animated;
    }

    public void setAnimateTime(int mAnimateTime) {
        this.mAnimateTime = mAnimateTime;
    }

    public boolean isClockWise() {
        return isClockWise;
    }

    public int getBorderBgColor() {
        return mBorderBgColor;
    }

    public int getBorderBgWidth() {
        return mBorderBgWidth;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public int getProgressWidth() {
        return mProgressWidth;
    }

    public boolean isAnimated() {
        return isAnimated;
    }

    public int getAnimateTime() {
        return mAnimateTime;
    }

    public void setMaxProgress(int mMaxProgress) {
        this.mMaxProgress = mMaxProgress;
    }

    public void setProgress(int mCurProgress) {
        this.mCurProgress = mCurProgress;
        int goalAngel = (int) (1.0f * mCurProgress / mMaxProgress * CIRCLE_ANGEL);
        goalAngel = goalAngel < 1 ? 1 : goalAngel;
        if (!isAnimated) {
            mCurSweepAngle = goalAngel;
            postInvalidate();
        } else {
            animateProgress(goalAngel);
        }
    }

    private void animateProgress(int goalAngel) {
        ValueAnimator va = ValueAnimator.ofInt(mCurSweepAngle, goalAngel);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mCurSweepAngle = value;
                invalidate();
            }
        });
        va.setInterpolator(new LinearInterpolator());
        va.setDuration(mAnimateTime);
        va.start();
    }

    private void init(AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        setWillNotDraw(false);
        initView(attrs);
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
    protected void initView(AttributeSet attrs) {
        mPaint.setAntiAlias(true);
        mBorderBgWidth =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_BORDER_BG_WIDTH, getResources
                        ().getDisplayMetrics());
        mProgressWidth =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_PROGRESS_WIDTH, getResources
                        ().getDisplayMetrics());
    }

    /**
     * 获取默认的长度
     *
     * @return
     */
    protected int getDefaultWidth() {
        return getDefaultHeight();
    }

    /**
     * 获取默认的高度
     *
     * @return
     */
    protected int getDefaultHeight() {
        return (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_WIDGET_SIZE, getContext().getResources
                        ().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = getWidth();
        int pivote = size / 2;
        int r = pivote - mBorderBgWidth;

        // 画边框圆
        mPaint.setColor(mBorderBgColor);
        mPaint.setStrokeWidth(mBorderBgWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(pivote, pivote, r, mPaint);

        // 画进度条
        r = pivote - mProgressWidth;
        mDrawRect.left = mProgressWidth;
        mDrawRect.top = mDrawRect.left;
        mDrawRect.right = pivote + r;
        mDrawRect.bottom = mDrawRect.right;
        mPaint.setColor(mProgressColor);
        mPaint.setStrokeWidth(mProgressWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(mDrawRect, mStartAngle, (isClockWise ? 1 : -1) * mCurSweepAngle, false, mPaint);
    }

}
