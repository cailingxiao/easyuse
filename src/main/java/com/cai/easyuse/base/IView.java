package com.cai.easyuse.base;

import android.content.Context;
import android.support.annotation.UiThread;

/**
 * mvp模式的view层需要继承的接口
 * <p>
 * Created by cailingxiao on 2016/10/4.
 */
public interface IView {
    @UiThread
    void showLoading();

    @UiThread
    void hideLoading();

    void toast(String msg);

    void finish();

    Context getContext();
}
