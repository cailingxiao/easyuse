package com.cai.easyuse.base.mark;

/**
 * 简单的回调
 * <p/>
 * Created by cailingxiao on 2016/10/6.
 */
public abstract class CallbackAdapter implements BuiCallback {
    @Override
    public void onSuccess(int statusCode, Object data) {

    }

    @Override
    public void onFail(int statusCode, Object error) {

    }
}
