package com.cai.easyuse.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基本的fragment控制类
 *
 * @author cailingxiao
 */
public abstract class BuiFragment extends Fragment {
    private View mRootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initView(mRootView);
        getActivity().getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            layzeLoadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 将事件送到该fragment所在的activity中执行
     *
     * @param action
     */
    protected void postUi(Runnable action) {
        if (!isActivityAlive()) {
            return;
        }
        getActivity().runOnUiThread(action);
    }

    /**
     * 该fragment依附的activity是否还存在
     *
     * @return
     */
    protected boolean isActivityAlive() {
        return getActivity() != null;
    }

    /**
     * 获取根布局
     *
     * @return
     */
    protected View getRootView() {
        return mRootView;
    }

    /**
     * 通过resId获取view
     *
     * @param resId
     * @return
     */
    protected <T extends View> T find(int resId) {
        return (T) mRootView.findViewById(resId);
    }

    /**
     * 获取根布局id，子类需要实现
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view，进行一些view的事件绑定等
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 做一些数据初始化的工作，会自动采用延缓加载策略
     */
    protected abstract void initData();

    /**
     * 页面可见时才加载的数据
     */
    protected void layzeLoadData() {

    }

}
