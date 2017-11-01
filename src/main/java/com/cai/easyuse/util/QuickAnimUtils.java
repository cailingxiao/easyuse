/**
 * @author cailingxiao
 * 2016年5月15日
 */
package com.cai.easyuse.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年5月15日 下午3:06:12
 * 类说明
 */

/**
 * 快捷使用动画的工具类，包含从上下左右四个方向出现和消失的动画。需要手动调用start方法
 *
 * @author cailingxiao
 * @date 2016年5月15日
 */
public final class QuickAnimUtils {
    private static final long DEFAULT_ANIM_DURATION = 800L;
    private static final String DEFAULT_PROPERTY_ALPHA = "alpha";
    private static final float ALPHA_MIN = 0.0f;
    private static final float ALPHA_MAX = 1.0f;
    private static final String DEFAULT_PROPERTY_TRANSLATION_X = "translationX";
    private static final String DEFAULT_PROPERTY_TRANSLATION_Y = "translationY";
    private static final float TRANSLATION_DELTA = 20.0f;

    private QuickAnimUtils() {

    }

    /**
     * 显示出来
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator show(View targetView, long duration) {
        if (View.VISIBLE != targetView.getVisibility()) {
            targetView.setVisibility(View.VISIBLE);
        }
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView, DEFAULT_PROPERTY_ALPHA,
                        ALPHA_MIN, ALPHA_MAX);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator show(View targetView) {
        return show(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 隐藏
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator hide(View targetView, long duration) {
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView, DEFAULT_PROPERTY_ALPHA,
                        ALPHA_MAX, ALPHA_MIN);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator hide(View targetView) {
        return hide(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 上升到布局的位置
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator up(View targetView, long duration) {
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView,
                        DEFAULT_PROPERTY_TRANSLATION_Y, TRANSLATION_DELTA, 0);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator up(View targetView) {
        return up(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 下降到布局的位置
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator down(View targetView, long duration) {
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView,
                        DEFAULT_PROPERTY_TRANSLATION_Y, -TRANSLATION_DELTA, 0);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator down(View targetView) {
        return down(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 左移到布局的位置
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator left(View targetView, long duration) {
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView,
                        DEFAULT_PROPERTY_TRANSLATION_X, TRANSLATION_DELTA, 0);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator left(View targetView) {
        return left(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 右移到布局位置
     *
     * @param targetView
     * @param duration
     * @return
     */
    public static ObjectAnimator right(View targetView, long duration) {
        ObjectAnimator oa =
                ObjectAnimator.ofFloat(targetView,
                        DEFAULT_PROPERTY_TRANSLATION_X, -TRANSLATION_DELTA, 0);
        oa.setDuration(duration);
        return oa;
    }

    public static ObjectAnimator right(View targetView) {
        return right(targetView, DEFAULT_ANIM_DURATION);
    }

    /**
     * 合成动画效果
     *
     * @param duration
     * @param anims
     * @return
     */
    public static AnimatorSet with(long duration, Animator... anims) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(anims);
        set.setDuration(duration);
        return set;
    }

    public static AnimatorSet with(Animator... anims) {
        return with(DEFAULT_ANIM_DURATION, anims);
    }

    /**
     * 顺序播放动画效果
     *
     * @param duration
     * @param anims
     * @return
     */
    public static AnimatorSet seq(long duration, Animator... anims) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(anims);
        set.setDuration(duration);
        return set;
    }

    /**
     * 按照自己的duration顺序播放
     *
     * @param anims
     * @return
     */
    public static AnimatorSet seq(Animator... anims) {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(anims);
        return set;
    }

}
