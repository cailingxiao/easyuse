package com.cai.easyuse.update.check;

import java.io.File;

import com.cai.easyuse.update.model.UpdateEntity;

import android.app.Activity;

/**
 * Created by cailingxiao on 2017/3/23.
 */
public class CheckUpdateAdapter implements ICheckUpdate {


    @Override
    public void onStartCheck() {

    }

    @Override
    public void onCheckResult(UpdateEntity entity, ICheckUpdateFeedBack feedback) {

    }

    @Override
    public void onCheckFailed(Object reason) {

    }

    @Override
    public void onUpdateVerifyFailed(UpdateEntity entity) {

    }

    @Override
    public void onUpdateVerifySuccess(UpdateEntity entity, File apkFile) {

    }

    @Override
    public void onDownloadStart(String url) {

    }

    @Override
    public void onDownloading(String url, long current, long total) {

    }

    @Override
    public void onDownloadFinished(String url, File file) {

    }
}
