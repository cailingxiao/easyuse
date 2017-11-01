package com.cai.easyuse.update.check;

import com.cai.easyuse.http.base.DownloadCallback;
import com.cai.easyuse.update.model.UpdateEntity;

/**
 * 更新状态回调接口，回调给内部使用
 * <p>
 * Created by cailingxiao on 2017/3/19.
 */
public interface ICheckUpdateFeedBack {
    /**
     * 用户选择了更新
     *
     * @param callback
     * @param entity
     */
    void onChooseUpdate(ICheckUpdate callback, UpdateEntity entity);

    /**
     * 用户选择了取消更新
     *
     * @param callback
     * @param entity
     */
    void onChooseFail(ICheckUpdate callback, UpdateEntity entity);
}
