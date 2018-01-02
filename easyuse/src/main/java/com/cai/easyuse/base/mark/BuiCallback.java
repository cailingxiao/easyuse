package com.cai.easyuse.base.mark;

/**
 * C层与M层回调业务接口
 *
 * @author cailingxiao
 */
public interface BuiCallback {

    /**
     * 业务逻辑成功
     *
     * @param data
     */
    void onSuccess(int statusCode, Object data);

    /**
     * 业务逻辑失败
     *
     * @param statusCode
     * @param error
     */
    void onFail(int statusCode, Object error);

}