package com.cai.easyuse.scan;

import android.app.Activity;
import android.content.Intent;

import com.cai.easyuse.scan.view.CaptureActivity;

/**
 * 扫描
 * <p>
 * Created by cailingxiao on 2017/11/1.
 */
public final class ScanApi {

    /**
     * 创建扫描intent
     *
     * @param activity
     *
     * @return
     */
    public static Intent createScanIntent(Activity activity) {
        return new Intent(activity, CaptureActivity.class);
    }
}
