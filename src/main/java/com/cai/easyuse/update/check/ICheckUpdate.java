package com.cai.easyuse.update.check;

import java.io.File;

import com.cai.easyuse.http.base.DownloadCallback;
import com.cai.easyuse.update.model.UpdateEntity;

/**
 * 更新状态回调接口，run on ui thread
 * <p>
 * Created by cailingxiao on 2017/3/19.
 */
public interface ICheckUpdate extends DownloadCallback {
    /**
     * 开始检查前调用
     */
    void onStartCheck();

    /**
     * 网络请求结果回调
     *
     * @param entity
     */
    void onCheckResult(UpdateEntity entity, ICheckUpdateFeedBack feedback);

    /**
     * 网络检查失败
     *
     * @param reason
     */
    void onCheckFailed(Object reason);

    /**
     * 更新校验不通过(版本校验，包名校验，签名校验)
     *
     * @param entity
     */
    void onUpdateVerifyFailed(UpdateEntity entity);

    /**
     * 校验通过
     */
    void onUpdateVerifySuccess(UpdateEntity entity, File apkFile);

}
