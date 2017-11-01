package com.cai.easyuse.image;

import java.io.File;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.cai.easyuse.R;
import com.cai.easyuse.base.mark.BuiCallback;
import com.cai.easyuse.util.LogUtils;
import com.cai.easyuse.util.MainThreadUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

/**
 * 图片加载
 * <p>
 * Created by cailingxiao on 2017/1/13.
 */
public final class ImgApi {
    private static final String TAG = "ImgApi";
    private static int sLoadingImg = R.mipmap.ic_loading;
    private static int sErrorImg = R.mipmap.ic_img_error;

    private ImgApi() {

    }

    /**
     * 加载网络图片,支持gif图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void load(String imgUrl, ImageView imageView) {
        Glide.with(imageView.getContext()).load(imgUrl).placeholder(sLoadingImg).error(sErrorImg)
                .animate(R.anim.item_alpha_in).into(imageView);
    }

    /**
     * 加载网络图片,支持gif图片
     *
     * @param imgUrl
     * @param imageView
     */
    public static void load(Context context, String imgUrl, ImageView imageView) {
        Glide.with(context).load(imgUrl).placeholder(sLoadingImg).error(sErrorImg)
                .animate(R.anim.item_alpha_in).into(imageView);
    }

    /**
     * 加载图片文件,支持gif图片
     *
     * @param file
     * @param imageView
     */
    public static void load(File file, ImageView imageView) {
        Glide.with(imageView.getContext()).load(file).placeholder(sLoadingImg).error(sErrorImg)
                .animate(R.anim.item_alpha_in).into(imageView);
    }

    /**
     * 加载本地resId文件
     *
     * @param resId
     * @param imageView
     */
    public static void load(int resId, ImageView imageView) {
        Glide.with(imageView.getContext()).load(resId).into(imageView);
    }

    /**
     * 显示times次gif文件
     *
     * @param resId
     * @param imageView
     * @param times     gif播放的次数
     */
    public static void loadGifWithTimes(int resId, ImageView imageView, int times) {
        Glide.with(imageView.getContext()).load(resId).into(new GlideDrawableImageViewTarget(imageView, times));
    }

    /**
     * 加载gif图，展示完成会回调callback的onSuccess
     *
     * @param resId
     */
    public static void loadGifWhenOver(int resId, ImageView imageView, final BuiCallback callback) {
        Glide.with(imageView.getContext()).load(resId).listener(new RequestListener<Integer, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Integer integer, Target<GlideDrawable> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, Integer integer, Target<GlideDrawable> target,
                                           boolean isFromMem, boolean isFirstResource) {
                int duration = 0;
                try {
                    GifDrawable gifDrawable = (GifDrawable) glideDrawable;
                    GifDecoder decoder = gifDrawable.getDecoder();
                    for (int i = 0; i < gifDrawable.getFrameCount(); i++) {
                        duration += decoder.getDelay(i);
                    }
                    MainThreadUtils.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onSuccess(0, null);
                            }
                        }
                    }, duration);
                } catch (Throwable e) {
                    LogUtils.print(e);
                }
                return false;
            }
        }).into(new GlideDrawableImageViewTarget(imageView, 1));
    }

    /**
     * 设置加载中的占位图
     *
     * @param imgResId
     */
    public static void setLoadingImg(int imgResId) {
        sLoadingImg = imgResId;
    }

    /**
     * 设置加载错误占位图
     *
     * @param imgResId
     */
    public static void setErrorImg(int imgResId) {
        sErrorImg = imgResId;
    }

}
