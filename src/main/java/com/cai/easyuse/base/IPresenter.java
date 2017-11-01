package com.cai.easyuse.base;

import android.support.annotation.UiThread;

/**
 * Created by niu on 2017/3/2.
 */
public interface IPresenter {

    @UiThread
    void release();
}
