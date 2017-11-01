package com.cai.easyuse.update;

import java.io.File;
import java.io.IOException;

import com.cai.easyuse.base.BasePresenter;
import com.cai.easyuse.update.check.CheckUpdateAdapter;
import com.cai.easyuse.update.check.ICheckUpdate;
import com.cai.easyuse.update.check.ICheckUpdateFeedBack;
import com.cai.easyuse.update.model.CheckUpdateModel;
import com.cai.easyuse.update.model.UpdateEntity;

import android.app.Activity;

/**
 * 检查更新，可进行普通更新、强制更新操作
 * <p>
 * Created by cailingxiao on 2017/3/19.
 */
public final class UpdateApi extends BasePresenter implements ICheckUpdateFeedBack {
    private static final String TAG = "UpdateApi";

    private static volatile UpdateApi sInstance = null;

    private CheckUpdateModel mModel = null;

    private UpdateApi() {
        mModel = new CheckUpdateModel();
    }

    public static UpdateApi getInstance() {
        if (null == sInstance) {
            synchronized (UpdateApi.class) {
                if (null == sInstance) {
                    sInstance = new UpdateApi();
                }
            }
        }
        return sInstance;
    }

    /**
     * 检查更新
     *
     * @param activity
     * @param callback
     */
    public void checkUpdate(final Activity activity, final ICheckUpdate callback) {
        if (null != callback && null != activity && !activity.isFinishing()) {
            executeInMainThread(new Runnable() {
                @Override
                public void run() {
                    callback.onStartCheck();
                }
            });
            execute(new Runnable() {
                @Override
                public void run() {
                    UpdateEntity entity = mModel.checkUpdate();
                    if (null != entity) {
                        callback.onCheckResult(entity, UpdateApi.this);
                    } else {
                        executeInMainThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onCheckFailed("检查失败");
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onChooseUpdate(final ICheckUpdate callback, final UpdateEntity entity) {
        try {
            mModel.downloadFile(entity, new CheckUpdateAdapter() {
                @Override
                public void onDownloadStart(final String url) {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onDownloadStart(url);
                            }
                        }
                    });

                }

                @Override
                public void onDownloading(final String url, final long current, final long total) {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onDownloading(url, current, total);
                            }
                        }
                    });

                }

                @Override
                public void onDownloadFinished(final String url, final File file) {
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                callback.onDownloadFinished(url, file);
                            }
                        }
                    });
                    final boolean success = mModel.verifyFile(file);
                    executeInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (null != callback) {
                                if (success) {
                                    callback.onUpdateVerifySuccess(entity, file);
                                } else {
                                    callback.onUpdateVerifyFailed(entity);
                                }
                            }
                        }
                    });
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChooseFail(ICheckUpdate callback, UpdateEntity entity) {
        try {
            mModel.silentDownload(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
