/**
 * @author cailingxiao
 * 2016年4月11日
 */
package com.cai.easyuse.base.holder;

import com.cai.easyuse.R;
import com.cai.easyuse.image.ImgApi;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年4月11日 下午11:49:25 
 * 类说明 
 */

/**
 * 通用的ViewHolder
 *
 * @author cailingxiao
 * @date 2016年4月11日
 */
public class BuiViewHolder {
    /**
     * 存储所有view的列表
     */
    private SparseArray<View> mViews;
    /**
     * 根view
     */
    private View mConvertView;

    public boolean isCycleUsed = false;

    /**
     * 构造函数
     *
     * @param ctx
     * @param parent
     * @param layoutId
     */
    private BuiViewHolder(Context ctx, ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(ctx).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        mViews = new SparseArray<View>();
        isCycleUsed = false;
    }

    /**
     * 获取一个ViewHolder
     *
     * @param ctx
     * @param convertView
     * @param parent
     * @param layoutId
     *
     * @return
     */
    public static BuiViewHolder get(Context ctx, View convertView,
                                    ViewGroup parent, int layoutId) {
        if (null == convertView) {
            return new BuiViewHolder(ctx, parent, layoutId);
        } else {
            BuiViewHolder holder = (BuiViewHolder) convertView.getTag();
            holder.isCycleUsed = true;
            return holder;
        }

    }

    /**
     * 获取具体的控件
     *
     * @param viewId
     *
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (null == view) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView控件添加文本
     *
     * @param textViewId
     * @param text
     *
     * @return
     */
    public BuiViewHolder setText(int textViewId, CharSequence text) {
        ((TextView) getView(textViewId)).setText(text);
        return this;
    }

    /**
     * 为ImageView控件添加图片
     *
     * @param imageViewId
     * @param url
     *
     * @return
     */
    public BuiViewHolder setImage(int imageViewId, String url) {
        if (!TextUtils.isEmpty(url)) {
            ImgApi.load(url, (ImageView) getView(imageViewId));
        } else {
            setImage(imageViewId, R.drawable.bui_loading);
        }
        return this;
    }

    public BuiViewHolder setImage(int imageView, Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            ((ImageView) getView(imageView)).setImageBitmap(bitmap);
        } else {
            setImage(imageView, R.drawable.bui_loading);
        }
        return this;
    }

    /**
     * 为ImageView控件添加图片，resId
     *
     * @param imageViewId
     * @param resId
     *
     * @return
     */
    public BuiViewHolder setImage(int imageViewId, int resId) {
        ((ImageView) getView(imageViewId)).setImageResource(resId);
        return this;
    }

    /**
     * 获取根布局
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }
}
