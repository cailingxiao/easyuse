/**
 * @author cailingxiao
 * 2016年4月12日
 */
package com.cai.easyuse.base.holder;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.UiThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/*
 * @author cailingxiao E-mail: cailingxiao2013@qq.com
 * @version 创建时间：2016年4月12日 上午12:01:49 
 * 类说明 
 */

/**
 * 通用的BaseAdapter
 *
 * @author cailingxiao
 * @date 2016年4月12日
 */
public abstract class BuiAdapter<T> extends BaseAdapter {
    private Context mContext;
    private List<T> mData;

    private int mLayoutId;

    public BuiAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public BuiAdapter(Context context, int layoutId, List<T> data) {
        mContext = context.getApplicationContext();
        mLayoutId = layoutId;
        mData = data;
    }

    protected Context getContext() {
        return mContext;
    }

    /**
     * 为Adapter添加数据,需要在ui线程中调用
     *
     * @param data
     */
    @UiThread
    public void setData(List<T> data) {
        if (null == data) {
            mData = null;
        } else {
            mData = new ArrayList<T>(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (null == mData) {
            return 0;
        } else {
            return mData.size();
        }
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BuiViewHolder viewHolder = BuiViewHolder.get(mContext, convertView, parent, mLayoutId);
        bindData(getItem(position), position, viewHolder);
        return viewHolder.getConvertView();
    }

    /**
     * 绑定数据到ui上
     *
     * @param itemData
     * @param position
     * @param viewHolder
     */
    @UiThread
    public abstract void bindData(T itemData, int position, BuiViewHolder viewHolder);

}
